package org.chaostocosmos.net.porta.managmenet;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

public class UtilBox {

    public static String getJsonString(Map map) {
        return new Gson().toJson(map);        
    }

    public static String getErrorJson(String errorCode, String errMessage) {
        Map<String, String> map = new HashMap<>();
        map.put("ERROR_CODE", errorCode);
        map.put("ERROR_MSG", errMessage);
        return getJsonString(map);
    }
}
