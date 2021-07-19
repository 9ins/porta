package org.chaostocosmos.porta;

import java.nio.charset.Charset;

/**
 * Constants
 * 
 * @author Kooin-Shin
 * @date 2020.11.30
 */
public class Constants {
    
    /**
     * Retry interval for Stand Alone mode
     */
    public static final long RETRY_INTERVAL = 3000;    

    /**
     * Default charset
     */
    public static final Charset defaultCharset = Charset.forName("utf-8");
}
