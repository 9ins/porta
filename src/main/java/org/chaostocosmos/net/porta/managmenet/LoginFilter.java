package org.chaostocosmos.net.porta.managmenet;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;

import org.chaostocosmos.net.porta.Logger;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginFilter implements Filter {
    

    @Override
    public void doFilter(jakarta.servlet.ServletRequest req, jakarta.servlet.ServletResponse res, FilterChain chain) throws IOException, jakarta.servlet.ServletException {
        HttpServletRequest request = (HttpServletRequest) req; 
        HttpServletResponse response = (HttpServletResponse) res;

        printRequest(request);

        //response.setHeader("Access-Control-Allow-Origin","*");
        //response.setHeader("Access-Control-Allow-Credentials", "true");
        //response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        //response.setHeader("Access-Control-Max-Age", "3600");
        //response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me");        

        HttpSession session = request.getSession(false);
        if(request.getHeader("username") != null) {
            System.out.println("&&&&&&&&&&&&&&&&&&&&&################");
            chain.doFilter(request, response);

        } 
        /*
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
        } else {
            System.out.println("&&&&&&&&&&&&&&&&&&&&&");
            chain.doFilter(request, response);
        }
        */
    }

    private void printRequest(HttpServletRequest request) {
        Logger.getInstance().info("##################### Request Method: " + request.getMethod() + " #####################");
        Logger.getInstance().info("Context Path: " + request.getContextPath() + "   session id: " + request.getRequestedSessionId());
        Logger.getInstance().info(request.getParameterMap().toString());
        Enumeration enu = request.getHeaderNames();
        while (enu.hasMoreElements()) {
            Object key = enu.nextElement();
            Logger.getInstance().info("HEADER KEY: "+ key + "  -  " + request.getHeader(key + ""));
        }
        enu = request.getAttributeNames();
        while (enu.hasMoreElements()) {
            Object key = enu.nextElement();
            Logger.getInstance().info("ATTR KEY: "+key+ "  -  " + request.getAttribute(key + ""));
        }
        Logger.getInstance().info("QUERY: " + request.getQueryString());
        Map<String, String[]> paramMap =  request.getParameterMap();
        System.out.println("PARMETER SIZE: "+paramMap.size());
        for(Map.Entry<String, String[]> entry : paramMap.entrySet()) {
            Logger.getInstance().info("PARAM KEY: "+entry.getKey()+"  -  "+Arrays.toString(entry.getValue()));
        }
        Logger.getInstance().info("###############################################################");
    }
}
