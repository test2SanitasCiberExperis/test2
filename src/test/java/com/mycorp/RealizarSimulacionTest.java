package com.mycorp;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import util.datos.UsuarioAlta;

import com.mycorp.spring.config.ZendeskConfig;

/**
 *
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
     * Rigourous Test :-)
     */
    @Test
    public void testApp() {
        // Preparado de datos
        UsuarioAlta usuarioAlta = new UsuarioAlta();
        String userAgent = "agente";
        // Test del servicio de polizas
        String altaTicket = zendeskService.altaTicketZendesk(usuarioAlta, userAgent);

        assertTrue(altaTicket != null && altaTicket.trim().compareTo("") != 0);
        assertTrue(true);
    }

}
