package org.chaostocosmos.net.porta.managmenet;

import java.util.Map;

public interface IResourceUsage {

    public String getCpuUsage(Map<String, String> paramMap) throws Exception;

    public String getMemoryUsage(Map<String, String> paramsMap) throws Exception;

    public String getThreadPoolUsage(Map<String, String> paramMap) throws Exception;
}
