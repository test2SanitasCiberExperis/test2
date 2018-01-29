package com.mycorp;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mycorp.constant.Constants;
import com.mycorp.support.Ticket;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.ListenableFuture;
import com.ning.http.client.Realm;
import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;
import com.ning.http.client.Response;
import com.ning.http.client.uri.Uri;

public class Zendesk implements Closeable {

    private AsyncHttpClient client;
    private Realm realm;
    private String url;
    private String oauthToken;
    private ObjectMapper mapper;
    private static boolean closeClient;
    private boolean closed = false;

    private static Logger logger = LoggerFactory.getLogger(Zendesk.class);

    /**
     *
     * Autenticación de usuario
     *
     * @param client
     *            petición http
     * @param url
     *            portal web donde autenticarse
     * @param username
     *            Usuario portal web
     * @param wdps
     *            Contraseña de usuario
     */
    public Zendesk(AsyncHttpClient client, String url, String username, String password) {
        closeClient = client == null;
        this.oauthToken = null;
        this.client = client == null ? new AsyncHttpClient() : client;
        this.url = url.endsWith("/") ? url + "api/v2" : url + "/api/v2";

        if (username != null) {
            this.realm = new Realm.RealmBuilder().setScheme(Realm.AuthScheme.BASIC).setPrincipal(username)
                    .setPassword(password).setUsePreemptiveAuth(true).build();
        } else {
            if (password != null) {
                throw new IllegalStateException("Cannot specify token or password without specifying username");
            }
            this.realm = null;
        }
        this.mapper = createMapper();
    }

    /**
     * Crea el ticket de autenticación
     *
     * @param ticket
     *            ticket
     * @return ticket procesado
     * @throws InterruptedException
     */
    public Ticket createTicket(Ticket ticket) throws InterruptedException {
        return ZendeskHelper.complete(submit(
                req("POST", cnst("/tickets.json"), Constants.JSON,
                        json(Collections.singletonMap(Constants.LITERAL_TICKET, ticket))),
                handle(Ticket.class, Constants.LITERAL_TICKET)));
    }

    /**
     * Transforma un objeto a bytes
     *
     * @param object
     *            objeto a transformar
     * @return objeto en bytes
     */
    private byte[] json(Object object) {
        try {
            return mapper.writeValueAsBytes(object);
        }
        catch (JsonProcessingException e) {
            throw new ZendeskException(e.getMessage(), e);
        }
    }

    /**
     * Construcción de la petición
     *
     * @param method
     *            método de la petición
     * @param template
     *            template de la petición
     * @param contentType
     *            contentType de la petición
     * @param body
     *            cuerto de petición
     * @return petición
     */
    private Request req(String method, Uri template, String contentType, byte[] body) {
        RequestBuilder builder = new RequestBuilder(method);
        if (realm != null) {
            builder.setRealm(realm);
        } else {
            builder.addHeader("Authorization", "Bearer " + oauthToken);
        }
        // replace out %2B with + due to API restriction
        builder.setUrl(Constants.RESTRICTED_PATTERN.matcher(template.toString()).replaceAll("+"));
        builder.addHeader("Content-type", contentType);
        builder.setBody(body);
        return builder.build();
    }

    /**
     * Creación del mapper
     *
     * @return mapper
     */
    public static ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }

    /**
     * Creación de la uri
     *
     * @param template
     *            template de la petición
     * @return uri de la petición
     */
    private Uri cnst(String template) {
        return Uri.create(url + template);
    }

    private <T> ListenableFuture<T> submit(Request request, BasicAsyncCompletionHandler<T> handler) {
        if (logger.isDebugEnabled()) {
            if (request.getStringData() != null) {
                logger.debug("Request {} {}\n{}", request.getMethod(), request.getUrl(), request.getStringData());
            } else if (request.getByteData() != null) {
                logger.debug("Request {} {} {} {} bytes", request.getMethod(), request.getUrl(), request.getHeaders()
                        .getFirstValue("Content-type"), request.getByteData().length);
            } else {
                logger.debug("Request {} {}", request.getMethod(), request.getUrl());
            }
        }
        return client.executeRequest(request, handler);
    }

    protected <T> ZendeskAsyncCompletionHandler<T> handle(final Class<T> clazz, final String name,
            final Class<?>... typeParams) {
        return new ZendeskAsyncCompletionHandler<>(clazz, name, typeParams);
    }

    private class ZendeskAsyncCompletionHandler<T> extends BasicAsyncCompletionHandler<T> {

        private Class<T> clazz;
        private String name;
        private Class[] typeParams;

        public ZendeskAsyncCompletionHandler(Class clazz, String name, Class... typeParams) {
            this.clazz = clazz;
            this.name = name;
            this.typeParams = typeParams;
        }

        @Override
        public T onCompleted(Response response) throws Exception {
            logResponse(response);
            if (isStatus2xx(response)) {
                if (typeParams.length > 0) {
                    JavaType type = mapper.getTypeFactory().constructParametrizedType(clazz, clazz, typeParams);
                    return mapper.convertValue(mapper.readTree(response.getResponseBodyAsStream()).get(name), type);
                }
                return mapper.convertValue(mapper.readTree(response.getResponseBodyAsStream()).get(name), clazz);
            } else if (isRateLimitResponse(response)) {
                throw new ZendeskException(response.toString());
            }
            if (response.getStatusCode() == 404) {
                return null;
            }
            throw new ZendeskException(response.toString());
        }

        @Override
        public void onThrowable(Throwable t) {
            if (t instanceof IOException) {
                throw new ZendeskException(t);
            } else {
                super.onThrowable(t);
            }
        }
    }

    // interface methods of Closeable
    public boolean isClosed() {
        return closed || client.isClosed();
    }

    // interface methods of Closeable
    @Override
    public void close() {
        if (closeClient && !client.isClosed()) {
            client.close();
        }
        closed = true;
    }

}