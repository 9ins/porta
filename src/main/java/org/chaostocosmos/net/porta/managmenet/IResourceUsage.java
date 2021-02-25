package org.chaostocosmos.net.porta.managmenet;

import java.util.Map;

import jakarta.servlet.http.HttpServletResponse;

public interface IResourceUsage {

    public String cpuProcess(Map<String, String> paramMap) throws Exception;

    public String memoryProcess(Map<String, String> paramsMap) throws Exception;

    public String sessionsThroughput(Map<String, String> paramsMap) throws Exception;

    
}
