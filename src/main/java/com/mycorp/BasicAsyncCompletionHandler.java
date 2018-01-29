package com.mycorp;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.Response;

public abstract class BasicAsyncCompletionHandler<T> extends AsyncCompletionHandler<T> {

    private static Logger logger = LoggerFactory.getLogger(Zendesk.class);

    public void logResponse(Response response) throws IOException {
        if (logger.isDebugEnabled()) {
            logger.debug("Response HTTP/{} {}\n{}", response.getStatusCode(), response.getStatusText(),
                    response.getResponseBody());
        }
        if (logger.isTraceEnabled()) {
            logger.trace("Response headers {}", response.getHeaders());
        }
    }

    public boolean isRateLimitResponse(Response response) {
        return response.getStatusCode() == 429;
    }

    /**
     * Devuelve el estado de la respuesta
     *
     * @param response
     *            resupesta de la petición
     * @return respuesta ok o ko
     */
    public boolean isStatus2xx(Response response) {
        return response.getStatusCode() / 100 == 2;
    }

}
