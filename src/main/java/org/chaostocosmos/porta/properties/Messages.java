package org.chaostocosmos.porta.properties;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
/**
 * Porta messages object
 */
public class Messages {

    public static enum MSG_TYPE {system, information, debug, error};

    Gson gson = new Gson();
    Map<String, String> system;
    Map<String, String> information;
    Map<String, String> debug;
    Map<String, String> error;

    public Messages() {}

    public Messages(Map<String, String> system, Map<String, String> information, Map<String, String> debug, Map<String, String> error) {
        this.system = system;
        this.information = information;
        this.debug = debug;
        this.error = error;
    }

    public Map<String, String> getSystem() {
        return this.system;
    }

    public void setSystem(Map<String, String> system) {
        this.system = system;
    }

    public Map<String, String> getInformation() {
        return this.information;
    }

    public void setInformation(Map<String, String> information) {
        this.information = information;
    }

    public Map<String,String> getDebug() {
        return this.debug;
    }

    public void setDebug(Map<String,String> debug) {
        this.debug = debug;
    }

    public Map<String, String> getError() {
        return this.error;
    }

    public void setError(Map<String, String> error) {
        this.error = error;
    }

    public String getMessage(MSG_TYPE type, String msgKey) {
        if(type == MSG_TYPE.system) {
            return this.system.get(msgKey);
        } else if(type == MSG_TYPE.information) {
            return this.information.get(msgKey);
        } else if(type == MSG_TYPE.error) {
            return this.error.get(msgKey);
        } else if(type == MSG_TYPE.debug) {
            return this.debug.get(msgKey);
        } else {
            throw new IllegalArgumentException("There isn't message exist matching with code: "+msgKey);
        }
    }

    public String getJson(MSG_TYPE type, String msgKey) {
        Map<String, String> map = new HashMap<>();
        map.put("code", msgKey);
        map.put("message", getMessage(type, msgKey));
        return gson.toJson(map);
    }

    public String getJson(String msgKey) {
        if(msgKey.startsWith("SYS")) {
            return getJson(MSG_TYPE.system, msgKey);
        } else if(msgKey.startsWith("INFO")) {
            return getJson(MSG_TYPE.information, msgKey);
        } else if(msgKey.startsWith("DEBUG")) {
            return getJson(MSG_TYPE.debug, msgKey);
        } else if(msgKey.startsWith("ERR")) {
            return getJson(MSG_TYPE.information, msgKey);
        } else {
            throw new IllegalArgumentException("Message key must start with SYS/INFO/DEBUG/ERR.");
        }
    }

    @Override
    public String toString() {
        return "{" +
            " gson='" + gson + "'" +
            ", system='" + system + "'" +
            ", information='" + information + "'" +
            ", debug='" + debug + "'" +
            ", error='" + error + "'" +
            "}";
    }
}
