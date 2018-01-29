package com.mycorp.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import com.mycorp.ZendeskService;
import com.mycorp.constant.ZendeskProperties;
import com.mycorp.support.impl.MensajeriaServiceImpl;
import com.mycorp.support.impl.PortalClientesWebEJBRemoteImpl;

@Configuration
@PropertySources({
                  @PropertySource(value = "classpath:envPC.properties"),
                  @PropertySource(value = "file:/etc/sanitas/app/zendesk/envPC.properties", ignoreResourceNotFound = true) })
public class ZendeskConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public ZendeskProperties zendeskProperties() {
        return new ZendeskProperties();
    }

    @Bean
    public ZendeskService getZendeskService() {
        return new ZendeskService(zendeskProperties());
    }

    @Bean
    public MensajeriaServiceImpl getMensajeriaService() {
        return new MensajeriaServiceImpl();
    }

    @Bean
    public PortalClientesWebEJBRemoteImpl getPortalclientesWebEJBRemoteImpl() {
        return new PortalClientesWebEJBRemoteImpl();
    }
}
