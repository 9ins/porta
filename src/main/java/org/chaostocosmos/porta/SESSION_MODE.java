package org.chaostocosmos.porta;

/**
 * 
 * SESSION_MODE
 *
 * @author 9ins
 * 2020. 11. 26.
 */
public enum SESSION_MODE {
	STAND_ALONE, 
	HIGH_AVAILABLE_FAIL_OVER,
	HIGH_AVAILABLE_FAIL_BACK,
	LOAD_BALANCE_ROUND_ROBIN,
	LOAD_BALANCE_SEPARATE_RATIO,
};

 