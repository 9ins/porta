package org.chaostocosmos.porta;

/**
 * Proxy Thread Pool Exception Handler
 */
public interface PortaThreadPoolExceptionHandler {

    /**
     * Handle exception
     * @param worker
     * @param t
     */
    public void handleException(Thread worker, Throwable t);
}
