package org.chaostocosmos.porta;

import org.chaostocosmos.porta.PortaSession.SessionTask;

/**
 * Proxy Thread Pool Exception Handler
 */
public interface PortaThreadPoolExceptionHandler {

    /**
     * Handing exception;
     * 
     * @param task
     * @param t
     */
    public void handleException(SessionTask task, Throwable t);
}
