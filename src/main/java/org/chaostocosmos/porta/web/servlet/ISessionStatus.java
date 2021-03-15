package org.chaostocosmos.porta.web.servlet;

import java.util.Map;

public interface ISessionStatus {

    public String getSessionInfo(String sessionName) throws Exception;

    public String getSessionsInfo() throws Exception;

    public String getSessionUsage(String sessionName) throws Exception;

    public String getSessionsUsage() throws Exception;

    public String getSessionThroughput(String sessionName) throws Exception;

    public String getSessionsThroughput() throws Exception;
}
