package org.chaostocosmos.porta;

import java.util.Map;

/**
 * Session status specification interface
 */
public interface ISessionStatus {

    /**
     * Get session information
     * @param sessionName
     * @return
     * @throws Exception
     */
    public Map<Object, Object> getSessionInfo(String sessionName) throws Exception;

    /**
     * Get sessions information
     * @return
     * @throws Exception
     */
    public Map<Object, Object> getSessionsInfo() throws Exception;

    /**
     * Get session usage information
     * @param sessionName
     * @return
     * @throws Exception
     */
    public Map<Object, Object> getSessionUsage(String sessionName) throws Exception;

    /**
     * Get sessions usage information
     * @return
     * @throws Exception
     */
    public Map<Object, Object> getSessionsUsage() throws Exception;

    /**
     * Get session throughput information
     * @param sessionName
     * @return
     * @throws Exception
     */
    public Map<Object, Object> getSessionThroughput(String sessionName) throws Exception;

    /**
     * Get sessions throughput information
     * @return
     * @throws Exception
     */
    public Map<Object, Object> getSessionsThroughput() throws Exception;
}
