package org.chaostocosmos.net.porta.managmenet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.lang.Character.UnicodeScript;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;

import com.google.gson.Gson;

import org.chaostocosmos.net.porta.Logger;
import org.chaostocosmos.net.porta.PortaMain;
import org.chaostocosmos.net.porta.managmenet.SystemMonitor.UNIT;

public class ResourceUsageServlet extends HttpServlet {

    Gson gson = new Gson();
    PortaMain portaMain;

    ResourceUsageServlet(PortaMain portaMain) {
        this.portaMain = portaMain;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("############# GET : "+request.getQueryString());

        System.out.println(request.getParameterMap().toString());
        String type = request.getParameter("type");
        String unit = request.getParameter("unit");
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("type", type);
        System.out.println("TYPE: "+type);
        if (type != null && type.equals("memory")) {
            try {
                float totalPhysicalMemory = SystemMonitor.getTotalPhysicalMemorySize(UNIT.valueOf(unit));
                float freePhysicalMemory = SystemMonitor.getFreePhysicalMemorySize(UNIT.valueOf(unit));
                float usedPhysicalMemory = totalPhysicalMemory - freePhysicalMemory;
                float maxHeapMemory = SystemMonitor.getProcessHeapMax(UNIT.valueOf(unit));
                float usedHeapMemory = SystemMonitor.getProcessHeapUsed(UNIT.valueOf(unit));
                float freeHeapMemory = maxHeapMemory - usedHeapMemory;
                map.put("totalPhysicalMemory", totalPhysicalMemory);
                map.put("freePhysicalMemory", freePhysicalMemory);
                map.put("usedPhysicalMemory", usedPhysicalMemory);
                map.put("maxHeapMemory", maxHeapMemory);
                map.put("usedHeapMemory", usedHeapMemory);
                map.put("freeHeapMemory", freeHeapMemory);
            } catch (AttributeNotFoundException | InstanceNotFoundException | MalformedObjectNameException | MBeanException | ReflectionException e) {
                Logger.getInstance().throwable(e);
            }
        } else if(type != null && type.equals("cpu")) {
            try {
                double cpuLoad = SystemMonitor.getProcessCpuLoad(UNIT.valueOf(unit));
                double cpuTime = SystemMonitor.getProcessCpuTime(UNIT.valueOf(unit));
                double systemCpuLoad = SystemMonitor.getSystemCpuLoad(UNIT.valueOf(unit));
                map.put("processCpuLoad", cpuLoad);
                map.put("processCpuTime", cpuTime);
                map.put("systemCpuLoad", systemCpuLoad);
            } catch (AttributeNotFoundException | InstanceNotFoundException | MalformedObjectNameException | MBeanException | ReflectionException e) {
                Logger.getInstance().throwable(e);
            }
        } else {
            map.put("type", "request error");
            map.put("msg", "Requested type: "+type+" was having wrong value. Resource request type just be allowed of 'memory' or 'cpu'.");
        }
        response.setContentType("application/json");
        System.out.println("SUCCESS : "+gson.toJson(map));
        response.getWriter().println(gson.toJson(map));        
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> reqMap = gson.fromJson(request.getReader(), Map.class);
        Logger.getInstance().info("REQ MAP: "+reqMap);
    }    
}
