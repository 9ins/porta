package org.chaostocosmos.porta;

import org.chaostocosmos.porta.PortaSession.ChannelWorker;

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
