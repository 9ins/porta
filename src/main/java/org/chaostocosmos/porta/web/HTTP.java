package org.chaostocosmos.porta.web;

/**
 * Http related class
 */
public class HTTP {
    
    public static enum METHOD {POST, GET, PUT, PATCH, DELETE};

    public static enum RESPONSE_TYPE {TEXT, JSON, HTML, XML, YAML, FILE};

    public static enum RESPONSE {CONTENT_TYPE, RESPONSE_CODE, RESPONSE_TYPE, RESPONSE_CONTENT};

}

