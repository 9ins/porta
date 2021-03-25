package org.chaostocosmos.porta;

import java.io.IOException;

import org.chaostocosmos.porta.properties.PropertiesHelper;

/**
 * 
 * ConfigException
 *
 * @author 9ins
 * 2020. 11. 26.
 */
public class PortaException extends Exception {

	String msgKey;
	static PropertiesHelper propertiesHelper = PropertiesHelper.getInstance();	

	/**
	 * Constructor
	 * .getMessage(MSG_TYPE.error, msgKey, messageParams)
	 * @param msgKey
	 * @param msg
	 */
	public PortaException(String msgKey, Object... params) {
		super(propertiesHelper.getErrorMessage(msgKey, params));
		this.msgKey = msgKey;
	}
	
	/**
	 * Get message key
	 * @return
	 */
	public String getMessageKey() {
		return this.msgKey;
	}

}
