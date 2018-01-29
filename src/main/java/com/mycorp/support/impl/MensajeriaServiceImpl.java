package com.mycorp.support.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mycorp.ZendeskService;
import com.mycorp.support.CorreoElectronico;
import com.mycorp.support.MensajeriaService;

public class MensajeriaServiceImpl implements MensajeriaService {

    private static Logger logger = LoggerFactory.getLogger(ZendeskService.class);

    @Override
    public void enviar(CorreoElectronico correo) {
        logger.info("email enviado");
    }

}
