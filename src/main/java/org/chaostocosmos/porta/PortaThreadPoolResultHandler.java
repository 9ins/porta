package org.chaostocosmos.porta;

/**
 * PortaThreadPoolResultHandler class
 */
public interface PortaThreadPoolResultHandler {

    /**
     * Handing channel worker task
     * @param worker
     * @param isSuccess
     */
    public void handleResult(ChannelWorker worker);
}
