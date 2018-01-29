package com.mycorp;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import util.datos.UsuarioAlta;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycorp.spring.config.ZendeskConfig;
import com.mycorp.support.DatosCliente;

/**
 * Unit test for simple App.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ZendeskConfig.class, loader = AnnotationConfigContextLoader.class)
public class RealizarSimulacionTest extends TestCase {

    /** The Constant logger. */
    private static Logger logger = LoggerFactory.getLogger(ZendeskService.class);

    @Autowired
    ZendeskService zendeskService;

    /**
     * Test por Tarjeta
     *
     * @throws JsonProcessingException
     * @throws ParseException
     */
    @Test
    public void testAppTarjeta() throws ZendeskException {

        // Preparado de datos
        UsuarioAlta usuarioAlta;
        try {
            usuarioAlta = getUsuarioAlta();
        }
        catch (ParseException e1) {
            logger.error("Error al obtener los datos de alta de usuario", e1);
            throw new ZendeskException(e1);
        }
        String userAgent = "agente";

        RestTemplate restTemplate = new RestTemplate();

        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);

        mockServer.expect(requestTo("http://localhost/123456")).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("111", MediaType.TEXT_PLAIN));

        try {
            mockServer.expect(requestTo("http://localhost:8080/test-endpoint")).andExpect(method(HttpMethod.GET))
                    .andRespond(withSuccess(getJsonDatosCliente(getDatosCliente()), MediaType.APPLICATION_JSON_UTF8));
        }
        catch (JsonProcessingException e) {
            logger.error("Error al obtener los datos de alta de cliente", e);
            throw new ZendeskException(e);
        }

        zendeskService.setRestTemplate(restTemplate);

        // Test del servicio de tarjetas
        String altaTicket = zendeskService.altaTicketZendesk(usuarioAlta, userAgent);

        assertTrue(altaTicket != null && altaTicket.trim().compareTo("") != 0);
    }

    /**
     * Test por Poliza
     *
     * @throws JsonProcessingException
     * @throws ParseException
     */
    @Test
    public void testAppPoliza() throws ZendeskException {

        // Preparado de datos
        UsuarioAlta usuarioAlta;
        try {
            usuarioAlta = getUsuarioAlta();
        }
        catch (ParseException e1) {
            logger.error("Error al obtener los datos de alta de usuario", e1);
            throw new ZendeskException(e1);
        }
        String userAgent = "agente";

        RestTemplate restTemplate = new RestTemplate();

        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);

        try {
            mockServer.expect(requestTo("http://localhost:8080/test-endpoint")).andExpect(method(HttpMethod.GET))
                    .andRespond(withSuccess(getJsonDatosCliente(getDatosCliente()), MediaType.APPLICATION_JSON_UTF8));
        }
        catch (JsonProcessingException e) {
            logger.error("Error al obtener los datos de alta de cliente", e);
            throw new ZendeskException(e);
        }

        zendeskService.setRestTemplate(restTemplate);

        usuarioAlta.setNumTarjeta(null);

        // Test del servicio de polizas
        String altaTicket = zendeskService.altaTicketZendesk(usuarioAlta, userAgent);

        assertTrue(altaTicket != null && altaTicket.trim().compareTo("") != 0);
    }

    /**
     * Obtiene los datos del cliente para el test
     *
     * @return datos del cliente
     */
    private DatosCliente getDatosCliente() {
        DatosCliente datosCliente = new DatosCliente();

        datosCliente.setIdCliente(Long.valueOf(124));
        datosCliente.setGenTTipoDocumento(1);
        datosCliente.setGenCTipoDocumento(2);
        datosCliente.setNumeroDocAcred("123456");
        datosCliente.setGenTCargo("Cargo");
        datosCliente.setGenCCargo("Cargo");
        datosCliente.setIdPais(2);
        datosCliente.setIdIdioma(2);
        datosCliente.setIdProfesion(2);
        datosCliente.setIdEntidadMotivo(2);
        datosCliente.setIdMotivo(2);
        datosCliente.setIdEntidadEstado(2);
        datosCliente.setIdEstado(2);
        datosCliente.setIdEmpresa(2);
        datosCliente.setIdDepartamento(2);
        datosCliente.setNombre("Pepe");
        datosCliente.setPrimerApellido("Santos");
        datosCliente.setSegundoApellido("Santos");
        datosCliente.setFechaNacimiento("01/01/1987");
        datosCliente.setNombreTarjeta("Pepe Santos");
        datosCliente.setCodEmpleado("123456");
        datosCliente.setSwAutorizaInformacion("Autorizado");
        datosCliente.setHorarioDesde("08:00");
        datosCliente.setHorarioHasta("17:00");
        datosCliente.setfEstado("01/01/2018");
        datosCliente.setfAlta("01/01/2017");
        datosCliente.setGenTTipoCliente(1);
        datosCliente.setGenCTipoCliente(1);
        datosCliente.setGenTStatus(1);
        datosCliente.setGenCStatus(1);
        datosCliente.setGenTPromocion(1);
        datosCliente.setGenCPromocion(1);
        datosCliente.setGenTSexo(1);
        datosCliente.setGenCSexo(1);
        datosCliente.setIdEntidadMotivoAlta(2);
        datosCliente.setIdMotivoAlta(2);
        datosCliente.setIdTratamiento(2);
        datosCliente.setSwFumador("No");
        datosCliente.setGenTEstadoCivil(1);
        datosCliente.setGenCEstadoCivil(1);
        datosCliente.setWdps("N");
        datosCliente.setIdClienteBupa("01012017");
        datosCliente.setNumHijos(2);
        datosCliente.setAlias("Pepe");
        datosCliente.setPreguntaSecreta("Pregunta");
        datosCliente.setRespuesta("Respuesta");
        datosCliente.setSwDnie("DNI");
        datosCliente.setfRegistroWeb("ReEgistro");
        datosCliente.setfInactivoWeb("Web");
        datosCliente.setIdClienteUhc(Long.valueOf(124));
        datosCliente.setSwCopagoSeparado("Copago");
        datosCliente.setSwConformidadSas("Sas");
        datosCliente.setGenTGrupoTmk(2);
        datosCliente.setGenCGrupoTmk(2);
        datosCliente.setAmigosPlus(2);
        datosCliente.setGeoBlue(Long.valueOf(32));

        return datosCliente;
    }

    /**
     *
     * Transforma los datos del cliente a Json Object
     *
     * @return datos del cliente transformado a Json
     *
     * @throws JsonProcessingException
     */
    private String getJsonDatosCliente(DatosCliente datosCliente) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        return mapper.writeValueAsString(datosCliente);

    }

    /**
     * Devuelve los datos de usuario de alta para el test
     *
     * @return datos de usuario de alta
     * @throws ParseException
     */
    private UsuarioAlta getUsuarioAlta() throws ParseException {
        UsuarioAlta usuarioAlta = new UsuarioAlta();

        usuarioAlta.setIndicadorDNI("N");
        usuarioAlta.setAutorizaInformacion("S");
        usuarioAlta.setNumPoliza("123456");
        usuarioAlta.setNumTarjeta("123456");
        usuarioAlta.setRegistroWEB(true);

        usuarioAlta.setAlias("Pepe");
        usuarioAlta.setPassword("Pepe");
        usuarioAlta.setRepitePass("Pepe");
        usuarioAlta.setPosicionPoliza(1);
        usuarioAlta.setPolizas(new ArrayList<>());
        usuarioAlta.setPreguntaSecreta("Como me llamo");
        usuarioAlta.setRespuesta("Pepe");
        usuarioAlta.setPrimerAccesoRegSani(true);

        usuarioAlta.setIdentificador("123456");
        usuarioAlta.setTipoPersona("F");
        usuarioAlta.setNombre("Pepe");
        usuarioAlta.setApellido1("Santos");
        usuarioAlta.setApellido2("Santos");
        usuarioAlta.setNumDocAcreditativo("123456");
        usuarioAlta.setTipoDocAcreditativo(1);
        usuarioAlta.setEmail("pepe@pepe.es");
        usuarioAlta.setPrefijoPais("34");
        usuarioAlta.setNumeroTelefono("611111111");
        usuarioAlta.setFNacimiento(new SimpleDateFormat("yyyyMMdd").parse("19900610"));
        usuarioAlta.setFAlta(new SimpleDateFormat("yyyyMMdd").parse("20170610"));
        usuarioAlta.setCodEmpleado("123456");
        usuarioAlta.setIdClieBupa("01012018");
        usuarioAlta.setSexo("M");
        usuarioAlta.setSwSeguridadSeccion("S");
        usuarioAlta.setSProfesion("Consultor");
        usuarioAlta.setTipoVia("C");
        usuarioAlta.setNombreVia("Alcala");
        usuarioAlta.setNumVia("1");
        usuarioAlta.setBloque("B");
        usuarioAlta.setEscalera("DCHA");
        usuarioAlta.setPiso("2");
        usuarioAlta.setPuerta("4");
        usuarioAlta.setResto("");
        usuarioAlta.setCodPostal("28080");
        usuarioAlta.setIMunicipio(1);
        usuarioAlta.setMunicipio("MADRID");
        usuarioAlta.setIProvincia(1);
        usuarioAlta.setProvincia("MADRID");
        usuarioAlta.setIdTipoVia(1);

        return usuarioAlta;
    }

}
