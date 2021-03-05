package org.chaostocosmos.net.porta.managmenet;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.Gson;

import org.chaostocosmos.net.porta.PortaMain;

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("############# GET : "+request.getQueryString()); 
        System.out.println(request.getParameterMap().toString());
        String type = request.getParameter("type");
        String unit = request.getParameter("unit");
        String resJson = null;
        switch(type) {
            case "sessionInfo" :
                resJson = getSessionInfo(request.getParameterMap());
                break;
            case "sessionsInfo" :
                resJson = getSessionsInfo(request.getParameterMap());
                break;
            case "sessionUsage" :
                resJson = getSessionUsage(request.getParameterMap());
                break;
            case "sessionsUsage" :
                resJson = getSessionsUsage(request.getParameterMap());
                break;
            case "sessionThrougput" :
                break;
            case "sessionsThrougput" :
                break;
            default : 
        }
        response.setContentType("application/json");
        System.out.println("SUCCESS : "+gson.toJson(map));
        response.getWriter().println(gson.toJson(map));        
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }        

    @Override
    public String getSessionInfo(Map<String, String[]> paramsMap) throws Exception {
        return null;
    }

    @Override
    public String getSessionsInfo(Map<String, String[]> paramMap) throws Exception {
        return null;
    }

    @Override
    public String getSessionUsage(Map<String, String[]> paramMap) throws Exception {
        return null;
    }

    @Override
    public String getSessionsUsage(Map<String, String[]> paramMap) throws Exception {
        return null;
    }

    @Override
    public String getSessionThroughput(Map<String, String[]> paramsMap) throws Exception {
        return null;
    }

    @Override
    public String getSessionsThroughput(Map<String, String[]> paramsMap) throws Exception {
        return null;
    }
}
