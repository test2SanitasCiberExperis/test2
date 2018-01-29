package com.mycorp;

import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ning.http.client.ListenableFuture;

public class ZendeskHelper {

    private static Logger logger = LoggerFactory.getLogger(ZendeskHelper.class);

    private ZendeskHelper() {
    }

    /**
     * Proporciona el resultado proceso asíncrono
     *
     * @param future
     *            listenableFuture
     * @return resultado del proceso asíncrono
     * @throws InterruptedException
     */
    public static <T> T complete(ListenableFuture<T> future) throws InterruptedException {
        try {
            return future.get();
        }
        catch (InterruptedException e) {
            logger.error("Proceso interrumpido: " + e);
            throw e;
        }
        catch (ExecutionException e) {
            if (e.getCause() instanceof ZendeskException) {
                throw (ZendeskException) e.getCause();
            }
            throw new ZendeskException(e.getMessage(), e);
        }
    }

}