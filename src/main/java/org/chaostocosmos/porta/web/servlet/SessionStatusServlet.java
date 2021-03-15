package org.chaostocosmos.porta.web.servlet;

import java.io.IOException;

import com.google.gson.Gson;

import org.chaostocosmos.porta.Context;
import org.chaostocosmos.porta.Logger;
import org.chaostocosmos.porta.PortaMain;
import org.chaostocosmos.porta.UtilBox;
import org.chaostocosmos.porta.properties.PropertiesHelper;
import org.chaostocosmos.porta.properties.SessionMappingConfigs;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * SessionStatusServlet
 * 
 */
public class SessionStatusServlet extends HttpServlet implements ISessionStatus {

    Gson gson = new Gson();
    PortaMain portaMain;

    public SessionStatusServlet(PortaMain portaMain) {
        this.portaMain = portaMain;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("############# GET : " + request.getQueryString());
        System.out.println(request.getParameterMap().toString());
        String type = request.getParameter("type");
        String name = request.getParameter("name");
        int status = HttpServletResponse.SC_OK;
        String resJson = null;
        try {
            switch (type) {
                case "sessionInfo":
                    resJson = getSessionInfo(name);
                    break;
                case "sessionsInfo":
                    resJson = getSessionsInfo();
                    break;
                case "sessionUsage":
                    resJson = getSessionUsage(name);
                    break;
                case "sessionsUsage":
                    resJson = getSessionsUsage();
                    break;
                case "sessionThrougput":
                    resJson = getSessionThroughput(name);
                    break;
                case "sessionsThrougput":
                    resJson = getSessionsThroughput();
                    break;
                default:
                    resJson = PropertiesHelper.getInstance().getErrorJson("ERRCODE001", new String[]{type});
                    status = HttpServletResponse.SC_BAD_REQUEST;
            }
        } catch (Exception e) {
            Logger.getInstance().throwable(e);
        }
        System.out.println("JSON : " + resJson);
        response.setContentType("application/json");
        response.getWriter().println(resJson);
        response.setStatus(status);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    public String getSessionInfo(String sessionName) throws Exception {
        SessionMappingConfigs sessionMapping = Context.configs.getSessionMappingConfigs(sessionName);        
        return UtilBox.getObjectToJson(sessionMapping);
    }

    @Override
    public String getSessionsInfo() throws Exception {        
        return UtilBox.getObjectToJson(Context.configs.getSessionMapping());
    }

    @Override
    public String getSessionUsage(String sessionName) throws Exception {
        return null;
    }

    @Override
    public String getSessionsUsage() throws Exception {
        return null;
    }

    @Override
    public String getSessionThroughput(String sessionName) throws Exception {
        return null;
    }

    @Override
    public String getSessionsThroughput() throws Exception {
        return null;
    }
}
