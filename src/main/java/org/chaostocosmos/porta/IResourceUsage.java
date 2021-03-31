package org.chaostocosmos.porta;

import java.util.Map;

/**
 * Resource usage specification interface
 */
public interface IResourceUsage {

    /**
     * Get CPU usage
     * @param unit
     * @return
     * @throws Exception
     */
    public Map<Object, Object> getCpuUsage(UNIT unit) throws Exception;

    /**
     * Get memory usage
     * @param unit
     * @return
     * @throws Exception
     */
    public Map<Object, Object> getMemoryUsage(UNIT unit) throws Exception;

    /**
     * Get thread pool infomation
     * @param unit
     * @return
     * @throws Exception
     */
    public Map<Object, Object> getThreadPoolUsage(UNIT unit) throws Exception;

    /**
     * Set core pool size
     * @param size
     * @throws Exception
     */
    public void setCorePoolSize(int size) throws Exception;

    /**
     * Set maxinum pool size
     * @param size
     * @throws Exception
     */
    public void setMaximumPoolSize(int size) throws Exception;
}
