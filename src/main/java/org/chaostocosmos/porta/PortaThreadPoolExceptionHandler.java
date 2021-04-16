package org.chaostocosmos.porta;


/**
 * Proxy Thread Pool Exception Handler
 */
public interface PortaThreadPoolExceptionHandler {

    /**
     * Handling exception
     * @param t
     */
    public void handleException(Throwable t);
}
