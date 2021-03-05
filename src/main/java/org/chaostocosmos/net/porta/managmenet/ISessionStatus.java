package org.chaostocosmos.net.porta.managmenet;

import java.util.Map;

public interface ISessionStatus {

    public String getSessionsInfo(Map<String, String[]> paramMap) throws Exception;

    public String getSessionInfo(Map<String, String[]> paramsMap) throws Exception;

    public String getSessionUsage(Map<String, String[]> paramMap) throws Exception;

    public String getSessionsUsage(Map<String, String[]> paramMap) throws Exception;
    
    public String getSessionThroughput(Map<String, String[]> paramsMap) throws Exception;

    public String getSessionsThroughput(Map<String, String[]> paramsMap) throws Exception;
}
