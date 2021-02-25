package org.chaostocosmos.net.porta;

import org.chaostocosmos.net.porta.ProxySession.SessionTask;

/**
 * Proxy Thread Pool Exception Handler
 */
public interface ProxyThreadPoolExceptionHandler {

    /**
     * Handing exception;
     * @param task
     * @param t
     */
    public void handleException(SessionTask task, Throwable t);
}
