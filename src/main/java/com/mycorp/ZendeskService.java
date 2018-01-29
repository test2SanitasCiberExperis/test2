package com.mycorp;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import util.datos.PolizaBasico;
import util.datos.UsuarioAlta;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mycorp.constant.Constants;
import com.mycorp.constant.ZendeskProperties;
import com.mycorp.support.CorreoElectronico;
import com.mycorp.support.DatosCliente;
import com.mycorp.support.Poliza;
import com.mycorp.support.PolizaBasicoFromPolizaBuilder;
import com.mycorp.support.Ticket;
import com.mycorp.support.ValueCode;
import com.mycorp.support.impl.MensajeriaServiceImpl;
import com.mycorp.support.impl.PortalClientesWebEJBRemoteImpl;

@Service
public class ZendeskService {

    /** The Constant logger. */
    private static Logger logger = LoggerFactory.getLogger(ZendeskService.class);

    ZendeskProperties zendeskProperties;

    RestTemplate restTemplate = new RestTemplate();

    public ZendeskService() {
        super();
    }

    public ZendeskService(ZendeskProperties zendeskProperties) {
        this.zendeskProperties = zendeskProperties;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Crea un ticket en Zendesk. Si se ha informado el número de tarjeta,
     * obtiene los datos asociados a dicha tarjeta de un servicio externo.
     *
     * @param usuarioAlta
     * @param userAgent
     * @return Los datos de usuario
     */
    public String altaTicketZendesk(UsuarioAlta usuarioAlta, String userAgent) {

        ObjectMapper mapper = new ObjectMapper();

        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        StringBuilder datosUsuario = new StringBuilder();
        StringBuilder datosBravo = new StringBuilder();

        String idCliente = null;

        StringBuilder clientName = new StringBuilder();

        datosUsuario.append(getDatosPoliza(usuarioAlta));
        datosUsuario.append("Tipo documento: ").append(usuarioAlta.getTipoDocAcreditativo())
                .append(Constants.ESCAPED_LINE_SEPARATOR);
        datosUsuario.append("Número documento: ").append(usuarioAlta.getNumDocAcreditativo())
                .append(Constants.ESCAPED_LINE_SEPARATOR);
        datosUsuario.append("Email personal: ").append(usuarioAlta.getEmail()).append(Constants.ESCAPED_LINE_SEPARATOR);
        datosUsuario.append("Número móvil: ").append(usuarioAlta.getNumeroTelefono())
                .append(Constants.ESCAPED_LINE_SEPARATOR);
        datosUsuario.append("User Agent: ").append(userAgent).append(Constants.ESCAPED_LINE_SEPARATOR);

        datosBravo.append(Constants.ESCAPED_LINE_SEPARATOR + "Datos recuperados de BRAVO:"
                + Constants.ESCAPED_LINE_SEPARATOR + Constants.ESCAPED_LINE_SEPARATOR);
        StringBuilder datosServicio = new StringBuilder();

        // Obtiene el idCliente de la tarjeta
        if (StringUtils.isNotBlank(usuarioAlta.getNumTarjeta())) {
            try {
                String urlToRead = zendeskProperties.getTarjetasGetDatos() + usuarioAlta.getNumTarjeta();
                ResponseEntity<String> res = restTemplate.getForEntity(urlToRead, String.class);
                if (res.getStatusCode() == HttpStatus.OK) {
                    String dusuario = res.getBody();
                    clientName.append(dusuario);
                    idCliente = dusuario;
                    datosServicio.append("Datos recuperados del servicio de tarjeta:")
                            .append(Constants.ESCAPED_LINE_SEPARATOR).append(mapper.writeValueAsString(dusuario));
                }
            }
            catch (Exception e) {
                logger.error("Error al obtener los datos de la tarjeta", e);
            }
        } else if (StringUtils.isNotBlank(usuarioAlta.getNumPoliza())) {
            try {
                Poliza poliza = new Poliza();
                poliza.setNumPoliza(Integer.valueOf(usuarioAlta.getNumPoliza()));
                poliza.setNumColectivo(Integer.valueOf(usuarioAlta.getNumDocAcreditativo()));
                poliza.setCompania(1);

                PolizaBasico polizaBasicoConsulta = new PolizaBasicoFromPolizaBuilder().withPoliza(poliza).build();

                PortalClientesWebEJBRemoteImpl portalclientesWebEJBRemoteImpl = new PortalClientesWebEJBRemoteImpl();

                final util.datos.DetallePoliza detallePolizaResponse = portalclientesWebEJBRemoteImpl
                        .recuperarDatosPoliza(polizaBasicoConsulta);

                clientName.append(detallePolizaResponse.getTomador().getNombre()).append(" ")
                        .append(detallePolizaResponse.getTomador().getApellido1()).append(" ")
                        .append(detallePolizaResponse.getTomador().getApellido2());

                idCliente = detallePolizaResponse.getTomador().getIdentificador();
                datosServicio.append("Datos recuperados del servicio de tarjeta:")
                        .append(Constants.ESCAPED_LINE_SEPARATOR)
                        .append(mapper.writeValueAsString(detallePolizaResponse));
            }
            catch (Exception e) {
                logger.error("Error al obtener los datos de la poliza", e);
            }
        }

        datosBravo.append(getDatosCliente(idCliente));

        createZendeskTicket(usuarioAlta, mapper, datosUsuario, datosBravo, clientName, datosServicio);

        datosUsuario.append(datosBravo);

        return datosUsuario.toString();
    }

    /**
     * Creación de ticket Zendesk
     *
     * @param usuarioAlta
     *            Datos de usuario de alta
     * @param mapper
     *            objeto mapper - * @param datosUsuario Datos de usuario
     * @param datosBravo
     *            Datos de poliza
     * @param clientName
     *            Nombre del cliente
     * @param datosServicio
     *            Datos del servicio
     *
     * @return si falla la creación del ticket
     */
    private boolean createZendeskTicket(UsuarioAlta usuarioAlta, ObjectMapper mapper, StringBuilder datosUsuario,
            StringBuilder datosBravo, StringBuilder clientName, StringBuilder datosServicio) {

        boolean falloCreacionTicket = false;

        String ticket = String
                .format(zendeskProperties.getPeticionZendesk(), clientName.toString(), usuarioAlta.getEmail(),
                        datosUsuario.toString() + datosBravo.toString() + parseJsonBravo(datosServicio));
        ticket = ticket.replaceAll("[" + Constants.ESCAPED_LINE_SEPARATOR + "]", " ");

        try (Zendesk zendesk = new Builder(zendeskProperties.getUrlZendesk())
                .setUsername(zendeskProperties.getZendeskUser()).setToken(zendeskProperties.getTokenZendesk()).build()) {
            // Ticket
            Ticket petiZendesk = mapper.readValue(ticket, Ticket.class);
            zendesk.createTicket(petiZendesk);

        }
        catch (Exception e) {
            logger.error("Error al crear ticket ZENDESK");
            // Send email

            CorreoElectronico correo = new CorreoElectronico(Long.parseLong(zendeskProperties
                    .getZendeskErrorDestinatario()), "es").addParam(
                    datosUsuario.toString().replaceAll(Constants.ESCAPE_ER + Constants.ESCAPED_LINE_SEPARATOR,
                            Constants.HTML_BR)).addParam(
                    datosBravo.toString().replaceAll(Constants.ESCAPE_ER + Constants.ESCAPED_LINE_SEPARATOR,
                            Constants.HTML_BR));
            correo.setEmailA(zendeskProperties.getZendeskErrorDestinatario());
            try {
                MensajeriaServiceImpl emailServiceImpl = new MensajeriaServiceImpl();
                emailServiceImpl.enviar(correo);
            }
            catch (Exception ex) {
                logger.error("Error al enviar mail", ex);
            }
            falloCreacionTicket = true;

        }

        return falloCreacionTicket;
    }

    /**
     * Obtiene datos del cliente
     *
     * @param idCliente
     * @return datos de la poliza
     */
    private StringBuilder getDatosCliente(String idCliente) {

        StringBuilder datosBravo = new StringBuilder();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        try {

            DatosCliente cliente = restTemplate.getForObject("http://localhost:8080/test-endpoint", DatosCliente.class,
                    idCliente);

            datosBravo.append("Teléfono: ").append(cliente.getGenTGrupoTmk()).append(Constants.ESCAPED_LINE_SEPARATOR);

            // DATE FORMAT CONSTANTS
            datosBravo.append("Feha de nacimiento: ")
                    .append(formatter.format(formatter.parse(cliente.getFechaNacimiento())))
                    .append(Constants.ESCAPED_LINE_SEPARATOR);

            List<ValueCode> tiposDocumentos = getTiposDocumentosRegistro();
            for (int i = 0; i < tiposDocumentos.size(); i++) {
                if (tiposDocumentos.get(i).getCode().equals(cliente.getGenCTipoDocumento().toString())) {
                    datosBravo.append("Tipo de documento: ").append(tiposDocumentos.get(i).getValue())
                            .append(Constants.ESCAPED_LINE_SEPARATOR);
                }
            }
            datosBravo.append("Número documento: ").append(cliente.getNumeroDocAcred())
                    .append(Constants.ESCAPED_LINE_SEPARATOR);

            datosBravo.append("Tipo cliente: ");
            switch (cliente.getGenTTipoCliente()) {
            case 1:
                datosBravo.append("POTENCIAL").append(Constants.ESCAPED_LINE_SEPARATOR);
                break;
            case 2:
                datosBravo.append("REAL").append(Constants.ESCAPED_LINE_SEPARATOR);
                break;
            case 3:
                datosBravo.append("PROSPECTO").append(Constants.ESCAPED_LINE_SEPARATOR);
                break;
            default:
                logger.warn("No hay GenTTipoCliente");
                break;
            }

            datosBravo.append("ID estado del cliente: ").append(cliente.getGenTStatus())
                    .append(Constants.ESCAPED_LINE_SEPARATOR);

            datosBravo.append("ID motivo de alta cliente: ").append(cliente.getIdMotivoAlta())
                    .append(Constants.ESCAPED_LINE_SEPARATOR);

            datosBravo.append("Registrado: ").append(cliente.getfInactivoWeb() == null ? "Sí" : "No")
                    .append(Constants.ESCAPED_LINE_SEPARATOR + Constants.ESCAPED_LINE_SEPARATOR);

        }
        catch (Exception e) {
            logger.error("Error al obtener los datos en BRAVO del cliente", e);
        }

        return datosBravo;
    }

    /**
     * Obtener datos poliza
     *
     * @param usuarioAlta
     *            datos de usuario
     *
     * @return datosUsuario datos de poliza
     */
    private StringBuilder getDatosPoliza(UsuarioAlta usuarioAlta) {

        StringBuilder datosUsuario = new StringBuilder();
        // Añade los datos del formulario
        if (StringUtils.isNotBlank(usuarioAlta.getNumPoliza())) {
            datosUsuario.append("Número de poliza/colectivo: ").append(usuarioAlta.getNumPoliza()).append("/")
                    .append(usuarioAlta.getNumDocAcreditativo()).append(Constants.ESCAPED_LINE_SEPARATOR);
        } else {
            datosUsuario.append("Número tarjeta Sanitas o Identificador: ").append(usuarioAlta.getNumTarjeta())
                    .append(Constants.ESCAPED_LINE_SEPARATOR);
        }

        return datosUsuario;
    }

    /**
     * Obtiene los tipos de documento de registro
     *
     * @return Lista de códigos
     */
    public List<ValueCode> getTiposDocumentosRegistro() {

        ValueCode vc1 = new ValueCode();
        vc1.setCode("1");
        vc1.setValue("Nif");

        ValueCode vc2 = new ValueCode();
        vc2.setCode("2");
        vc2.setValue("Tarjeta");

        // simulacion servicio externo
        return Arrays.asList(vc1, vc2);
    }

    /**
     * Método para parsear el JSON de respuesta de los servicios de
     * tarjeta/póliza
     *
     * @param resBravo
     * @return JSON parseado
     */
    private String parseJsonBravo(StringBuilder resBravo) {
        return resBravo.toString().replaceAll("[\\[\\]\\{\\}\\\"\\r]", "")
                .replaceAll(Constants.ESCAPED_LINE_SEPARATOR, Constants.ESCAPE_ER + Constants.ESCAPED_LINE_SEPARATOR);
    }
}