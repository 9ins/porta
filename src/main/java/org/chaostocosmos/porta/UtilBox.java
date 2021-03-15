package org.chaostocosmos.porta;

import java.util.Map;

import com.google.gson.Gson;

public class UtilBox {

    static Gson gson = new Gson();

    public static String getJsonString(Map map) {
        return gson.toJson(map);        
    }

    public static String getMapToJson(Map map) {
        return getJsonString(map);
    }

    public static String getObjectToJson(Object obj) {
        return gson.toJson(obj);
    }
}
