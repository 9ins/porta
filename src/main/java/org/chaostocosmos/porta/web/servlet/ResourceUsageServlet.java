package org.chaostocosmos.porta.web.servlet;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.Gson;

import org.chaostocosmos.porta.Logger;
import org.chaostocosmos.porta.PortaMain;
import org.chaostocosmos.porta.SystemMonitor;
import org.chaostocosmos.porta.SystemMonitor.UNIT;
import org.chaostocosmos.porta.properties.PropertiesHelper;
import org.chaostocosmos.porta.properties.Messages.MSG_TYPE;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ResourceUsageServlet extends HttpServlet implements IResourceUsage {

    Gson gson = new Gson();
    PortaMain portaMain;

    public ResourceUsageServlet(PortaMain portaMain) {
        this.portaMain = portaMain;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("############# GET : "+request.getQueryString());
        System.out.println(request.getParameterMap().toString());
        String type = request.getParameter("type");
        String unit = request.getParameter("unit");
        Map<String, String> map = new LinkedHashMap<>();
        map.put("type", type);
        map.put("unit", unit);
        System.out.println("TYPE: "+type);
        String resJson = null;
        try {
            switch(type) {
                case "cpu" :
                    resJson = getCpuUsage(map);
                break;
                case "memory" :
                    resJson = getMemoryUsage(map);
                break;
                default:
                    resJson = PropertiesHelper.getInstance().getMessages().getMessage(MSG_TYPE.error, "ERRCODE001");
            }
        } catch (Exception e) {
            Logger.getInstance().throwable(e);
        }
        response.setContentType("application/json");
        System.out.println("SUCCESS : "+resJson);
        response.getWriter().println(resJson);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> reqMap = gson.fromJson(request.getReader(), Map.class);
        Logger.getInstance().info("REQ MAP: "+reqMap);
    }

    @Override
    public String getCpuUsage(Map<String, String> paramMap) throws Exception {
        String unit = paramMap.get("unit");
        double cpuLoad = SystemMonitor.getProcessCpuLoad(UNIT.valueOf(unit));
        double cpuTime = SystemMonitor.getProcessCpuTime(UNIT.valueOf(unit));
        double systemCpuLoad = SystemMonitor.getSystemCpuLoad(UNIT.valueOf(unit));
        paramMap.put("CpuLoad", cpuLoad+"");
        paramMap.put("CpuTime", cpuTime+"");
        paramMap.put("SystemCpuLoad", systemCpuLoad+"");
        return gson.toJson(paramMap);
    }

    @Override
    public String getMemoryUsage(Map<String, String> paramsMap) throws Exception {
        String unit = paramsMap.get("unit");
        float totalPhysicalMemory = SystemMonitor.getTotalPhysicalMemorySize(UNIT.valueOf(unit));
        float freePhysicalMemory = SystemMonitor.getFreePhysicalMemorySize(UNIT.valueOf(unit));
        float usedPhysicalMemory = totalPhysicalMemory - freePhysicalMemory;
        float maxHeapMemory = SystemMonitor.getProcessHeapMax(UNIT.valueOf(unit));
        float usedHeapMemory = SystemMonitor.getProcessHeapUsed(UNIT.valueOf(unit));
        float freeHeapMemory = maxHeapMemory - usedHeapMemory;
        float usedMemory = SystemMonitor.getProcessMemoryUsed(UNIT.valueOf(unit));
        paramsMap.put("SystemTotal", totalPhysicalMemory+"");
        paramsMap.put("SystemFree", freePhysicalMemory+"");
        paramsMap.put("SystemUsed", usedPhysicalMemory+"");
        paramsMap.put("HeapMax", maxHeapMemory+"");
        paramsMap.put("HeapUsed", usedHeapMemory+"");
        paramsMap.put("HeapFree", freeHeapMemory+"");
        paramsMap.put("MemoryUsed", usedMemory+"");                
        return gson.toJson(paramsMap);
    }

    @Override
    public String getThreadPoolUsage(Map<String, String> paramMap) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }    
}
