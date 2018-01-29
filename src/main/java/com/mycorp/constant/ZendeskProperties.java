package com.mycorp.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ZendeskProperties {

    @Value("${zendesk.ticket}")
    private String peticionZendesk;

    @Value("${zendesk.token}")
    public String tokenZendesk = "";

    @Value("${zendesk.url}")
    public String urlZendesk = "";

    @Value("${zendesk.user}")
    public String zendeskUser = "";

    @Value("${tarjetas.getdatos}")
    public String tarjetasGetDatos = "";

    @Value("${zendesk.error.mail.funcionalidad}")
    public String zendeskErrorMailFuncionalidad = "";

    @Value("${zendesk.error.destinatario}")
    public String zendeskErrorDestinatario = "";

    public String getPeticionZendesk() {
        return peticionZendesk;
    }

    public String getTokenZendesk() {
        return tokenZendesk;
    }

    public String getUrlZendesk() {
        return urlZendesk;
    }

    public String getZendeskUser() {
        return zendeskUser;
    }

    public String getTarjetasGetDatos() {
        return tarjetasGetDatos;
    }

    public String getZendeskErrorMailFuncionalidad() {
        return zendeskErrorMailFuncionalidad;
    }

    public String getZendeskErrorDestinatario() {
        return zendeskErrorDestinatario;
    }

}
