package org.chaostocosmos.porta;

import java.util.List;
import java.util.Map;

/**
 * Session status specification interface
 */
public interface ISessionStatus {

    /**
     * Get session simple information
     * @return
     * @throws Exception
     */
    public Map<Object, Object> getSessionSimple() throws Exception;

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

    /**
     * Apply session configuration to current active session.
     * @param sessionName
     * @param sessionInfoMap
     * @throws Exception
     */
    public void applySessionInfo(String sessionName, Map<Object, Object> sessionInfoMap) throws Exception;

    /**
     * Save session configuration to config YAML file in config directory.
     * @param sessionName
     * @param sessionInfoMap
     * @throws Exception
     */
    public void saveSessionInfo(String sessionName, Map<Object, Object> sessionInfoMap) throws Exception;

    /**
     * Apply all session configurations to all active sessions.
     * @param sessionsInfoMap
     * @throws Exception
     */
    public void applySessionInfos(Map<String, Map<Object, Object>> sessionsInfoMap) throws Exception;

    /**
     * Save all session configurations to all active sessions.
     * @param sessionsInfoMap
     * @throws Exception
     */
    public void saveSessionInfos(Map<String, Map<Object, Object>> sessionsInfoMap) throws Exception;
}
