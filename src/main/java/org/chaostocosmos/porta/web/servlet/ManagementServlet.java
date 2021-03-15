package org.chaostocosmos.porta.web.servlet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.chaostocosmos.porta.PortaMain;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ManagementServlet extends HttpServlet {

    PortaMain tcpProxy;
    String sessionId = "";

    Map<String, Long> sessionIdMap = new HashMap<>();

    public ManagementServlet(PortaMain tcpProxy) {
        this.tcpProxy = tcpProxy;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        printRqeust(request);
        System.out.println("Session id: "+request.getSession().getId());
        System.out.println("Request id: "+request.getRequestedSessionId());
        System.out.println("isNew: "+request.getSession().isNew());

        String requestId = request.getRequestedSessionId();
        Long lastTimeMillis = sessionIdMap.get(requestId);
        if(lastTimeMillis != null && lastTimeMillis+5000 < System.currentTimeMillis()) {
            request.logout();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }        
        sessionIdMap.put(requestId, System.currentTimeMillis());
        Cookie cookie = new Cookie("JSESSIONID", request.getSession().getId());
        cookie.setMaxAge(10);
        response.addCookie(cookie);
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(Files.readAllLines(Paths.get("D:\\Projects\\TCPProxy\\porta-www\\build\\index.html")).stream().map(l -> l).collect(Collectors.joining(System.lineSeparator())));
    }

    protected void printRqeust(HttpServletRequest request) {
        this.sessionIdMap.keySet().stream().forEach(k -> System.out.println("User : "+k+"   Id : "+sessionIdMap.get(k)));
        Enumeration<String> enu = request.getHeaderNames();
        while(enu.hasMoreElements()) {
            String key = enu.nextElement();
            System.out.println("Name : "+key+"  Value: "+request.getHeader(key));
        }
        enu = request.getAttributeNames();
        while(enu.hasMoreElements()) {
            String key = enu.nextElement();
            System.out.println("Attr name : "+key+"  Attr value: "+request.getAttribute(key));
        }
        System.out.println("Context Path: "+request.getContextPath()+"   Request Session Id: "+request.getRequestedSessionId());
        System.out.println(
            "isRequestedSessionIdValid: "+request.isRequestedSessionIdValid()+"   \n"+
            "isRequestedSessionIdFromCookie: "+request.isRequestedSessionIdFromCookie()+"   \n"+
            "isRequestedSessionIdFromURL: "+request.isRequestedSessionIdFromURL()+"   \n"+            
            "getMaxInactiveInterval: "+request.getSession().getMaxInactiveInterval()+"   \n"+
            //"getMaxInactiveInterval: "+request.getSession().getAttribute("state").toString()+"   \n"+
            "isUserInRole: "+request.isUserInRole("ADMIN") +"   \n"+
            "user: "+request.getParameter("username") +"   \n"
            );
    }

}
