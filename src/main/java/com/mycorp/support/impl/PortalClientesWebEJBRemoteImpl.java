package com.mycorp.support.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import portalclientesweb.ejb.interfaces.PortalClientesWebEJBRemote;
import portalclientesweb.ejb.vo.DatosRetorno;
import util.datos.ActoMedicoConsulta;
import util.datos.ClaveOperativa;
import util.datos.CopagoBean;
import util.datos.DatosClientes;
import util.datos.DatosPersonales;
import util.datos.DetallePoliza;
import util.datos.Formulario;
import util.datos.PolizaBasico;
import util.datos.PrestadorBasico;
import util.datos.PrestadorDetalle;
import util.datos.ReciboConsulta;
import util.datos.ReciboDetalle;
import util.datos.ReembolsoConsulta;
import util.datos.Usuario;
import util.datos.UsuarioAlta;

public class PortalClientesWebEJBRemoteImpl implements PortalClientesWebEJBRemote {

    @Override
    public List recuperarEstados(Map paramMap) {

        return null;
    }

    @Override
    public List recuperarMunicipios(Map paramMap) {

        return null;
    }

    @Override
    public List recuperarProvincias(Map paramMap) {

        return null;
    }

    @Override
    public List recuperarComunidades(Map paramMap) {

        return null;
    }

    @Override
    public List recuperarGeneTablas(int paramInt1, int paramInt2) {

        return null;
    }

    @Override
    public DatosPersonales recuperarDatosCliente(String paramString) {

        return null;
    }

    @Override
    public DetallePoliza recuperarDatosPoliza(PolizaBasico paramPolizaBasico) {
        DetallePoliza detallePoliza = new DetallePoliza();

        detallePoliza.setTomador(new DatosPersonales());

        detallePoliza.getTomador().setNombre("Pepe");
        detallePoliza.getTomador().setApellido1("Santos");
        detallePoliza.getTomador().setApellido2("Santos");
        detallePoliza.getTomador().setIdentificador("123456");

        return detallePoliza;
    }

    @Override
    public List recuperarCoberturasCapitales(Map paramMap) {

        return null;
    }

    @Override
    public Map recuperarPolizaCorreoTelefono(PolizaBasico paramPolizaBasico) {

        return null;
    }

    @Override
    public DetallePoliza recuperarPolizaDireccion(DetallePoliza paramDetallePoliza) {

        return null;
    }

    @Override
    public List recuperarPrimasAseg(PolizaBasico paramPolizaBasico) {

        return null;
    }

    @Override
    public List listaBeneficiariosBasica(PolizaBasico paramPolizaBasico, String paramString1, String paramString2) {

        return null;
    }

    @Override
    public List listaBeneficiariosDetalle(PolizaBasico paramPolizaBasico) {

        return null;
    }

    @Override
    public String calcularPerfilCliente(String paramString, PolizaBasico paramPolizaBasico) {

        return null;
    }

    @Override
    public Map alta(UsuarioAlta paramUsuarioAlta) {

        return null;
    }

    @Override
    public Integer modificarDatosUsuario(Map paramMap) {

        return null;
    }

    @Override
    public Usuario recuperarContraseniaRecordatorio(Map paramMap) {

        return null;
    }

    @Override
    public Map validarAccesoUsuario(Map paramMap) {

        return null;
    }

    @Override
    public Map validarAccesoIdCliente(String paramString) {

        return null;
    }

    @Override
    public Map listaPolizas(Map paramMap) {

        return null;
    }

    @Override
    public int bajaUsuarioWeb(Map paramMap) {

        return 0;
    }

    @Override
    public Map buscarActosPoliza(PolizaBasico paramPolizaBasico, ActoMedicoConsulta paramActoMedicoConsulta,
            int paramInt1, int paramInt2) {

        return null;
    }

    @Override
    public CopagoBean buscarCopagosPoliza(PolizaBasico paramPolizaBasico, String paramString1, String paramString2,
            int paramInt1, int paramInt2, int paramInt3) {

        return null;
    }

    @Override
    public List buscarPeriodosCopago(PolizaBasico paramPolizaBasico) {

        return null;
    }

    @Override
    public Integer calcularFechaPeriodo(Integer paramInteger) {

        return null;
    }

    @Override
    public Map buscarRecibos(PolizaBasico paramPolizaBasico, ReciboConsulta paramReciboConsulta, int paramInt1,
            int paramInt2, List paramList) {

        return null;
    }

    @Override
    public Map detalleRecibos(ReciboConsulta paramReciboConsulta, ReciboDetalle paramReciboDetalle) {

        return null;
    }

    @Override
    public List buscarLimites(Map paramMap) throws Exception {

        return null;
    }

    @Override
    public Map recuperarAliasUsuario(Map paramMap) {

        return null;
    }

    @Override
    public Map buscarReembolsos(PolizaBasico paramPolizaBasico, ReembolsoConsulta paramReembolsoConsulta,
            int paramInt1, int paramInt2, List paramList) {

        return null;
    }

    @Override
    public ReembolsoConsulta detalleReembolsos(ReembolsoConsulta paramReembolsoConsulta) {

        return null;
    }

    @Override
    public List getListaDetallesReembolsos(ReembolsoConsulta paramReembolsoConsulta) {

        return null;
    }

    @Override
    public Map recuperarNumTarjetaCliente(String paramString, PolizaBasico paramPolizaBasico) {

        return null;
    }

    @Override
    public Map recuperarVersionTarjetaCliente(String paramString, PolizaBasico paramPolizaBasico) {

        return null;
    }

    @Override
    public Map recuperarNombreSucursal(String paramString1, String paramString2, Formulario paramFormulario) {

        return null;
    }

    @Override
    public Formulario recuperarCCC(PolizaBasico paramPolizaBasico) {

        return null;
    }

    @Override
    public Map buscarPrestadores(PrestadorBasico paramPrestadorBasico) {

        return null;
    }

    @Override
    public PrestadorDetalle detallePrestadores(String paramString) {

        return null;
    }

    @Override
    public ClaveOperativa buscarClaveOperativa(String paramString) {

        return null;
    }

    @Override
    public Map buscarPrestadorListaID(List paramList) {

        return null;
    }

    @Override
    public Map validaPeticionUsuario(Map paramMap) {

        return null;
    }

    @Override
    public Map grabaPeticionUsuario(Map paramMap) {

        return null;
    }

    @Override
    public Map fechaUltimaPeticion(Map paramMap) {

        return null;
    }

    @Override
    public DetallePoliza recuperarEmpresaDepartamento(DetallePoliza paramDetallePoliza) {

        return null;
    }

    @Override
    public Map grabarDatos(Map paramMap) {

        return null;
    }

    @Override
    public List listaMotivosSolictudTarjeta() {

        return null;
    }

    @Override
    public Integer actualizaEstadoPeticion(Integer paramInteger, boolean paramBoolean) {

        return null;
    }

    @Override
    public List listaPaises(Long paramLong) {

        return null;
    }

    @Override
    public int validarTarjeta(long paramLong, String paramString) {

        return 0;
    }

    @Override
    public Usuario validarUsuarioWP(String paramString1, String paramString2) {

        return null;
    }

    @Override
    public Usuario validarDatosRegistro(UsuarioAlta paramUsuarioAlta) {

        return null;
    }

    @Override
    public int confirmarRegistro(String paramString) {

        return 0;
    }

    @Override
    public List buscarCuestionarioSalud(int paramInt1, int paramInt2) {

        return null;
    }

    @Override
    public List buscarCuestionarioDetalle(int paramInt1, int paramInt2) {

        return null;
    }

    @Override
    public UsuarioAlta ejecutarRecordatorioContrasenia(UsuarioAlta paramUsuarioAlta) {

        return null;
    }

    @Override
    public Integer setExtractoCopago(Long paramLong1, Long paramLong2, Long paramLong3, String paramString) {

        return null;
    }

    @Override
    public String getExtractoCopago(Long paramLong1, Long paramLong2, Long paramLong3) {

        return null;
    }

    @Override
    public Map recuperarDatosTarjeta(String paramString) {

        return null;
    }

    @Override
    public List guardarNuevoCliente(DatosClientes paramDatosClientes) {

        return null;
    }

    @Override
    public long getSecuencia(String paramString1, String paramString2) {

        return 0;
    }

    @Override
    public List recuperaVentajas(Long paramLong1, Long paramLong2, Long paramLong3, Boolean paramBoolean1,
            Boolean paramBoolean2, Timestamp paramTimestamp, String paramString) {

        return null;
    }

    @Override
    public List recuperaVentajas(Long paramLong1, Long paramLong2, Long paramLong3, Boolean paramBoolean1,
            Boolean paramBoolean2, Timestamp paramTimestamp, String paramString, Long paramLong4) {

        return null;
    }

    @Override
    public byte[] recuperaMaxNumEnvio(String paramString1, String paramString2, String paramString3) throws Exception {

        return null;
    }

    @Override
    public HashMap recuperarDatosClienteXDNI(Long paramLong, String paramString) {

        return null;
    }

    @Override
    public HashMap recuperarListaDatosClienteXDNI(Long paramLong, String paramString, int paramInt) {

        return null;
    }

    @Override
    public HashMap recuperarDatosClienteRecupContXDNI(String paramString) {

        return null;
    }

    @Override
    public List obtenerPreexistencias(PolizaBasico paramPolizaBasico) {

        return null;
    }

    @Override
    public Map validaAccesoUsuarioDNIAlias(String paramString1, String paramString2, Boolean paramBoolean) {

        return null;
    }

    @Override
    public Map grabaPeticionUsuarioDupli(Map paramMap) {

        return null;
    }

    @Override
    public DatosRetorno altaCliente(String paramString1, Long paramLong1, Long paramLong2, Timestamp paramTimestamp,
            Integer paramInteger, String paramString2, String paramString3, String paramString4, String paramString5,
            String paramString6, String paramString7) {

        return null;
    }

    @Override
    public DatosRetorno altaClienteProspect(String paramString1, String paramString2, String paramString3,
            String paramString4, Timestamp paramTimestamp, Integer paramInteger, String paramString5,
            String paramString6, String paramString7, String paramString8, String paramString9) {

        return null;
    }

    @Override
    public DatosRetorno recordatorioContrasenya(String paramString1, Long paramLong1, Long paramLong2,
            Integer paramInteger, String paramString2, Timestamp paramTimestamp) {

        return null;
    }

    @Override
    public DatosRetorno login(String paramString1, String paramString2, Boolean paramBoolean) {

        return null;
    }

    @Override
    public Map solicitudDuplicadoDocumentacion(Map paramMap) {

        return null;
    }

    @Override
    public List recuperaVentajas(Long paramLong1, Long paramLong2, Long paramLong3, Boolean paramBoolean1,
            Boolean paramBoolean2, Boolean paramBoolean3, Timestamp paramTimestamp, String paramString, Long paramLong4) {

        return null;
    }
}
