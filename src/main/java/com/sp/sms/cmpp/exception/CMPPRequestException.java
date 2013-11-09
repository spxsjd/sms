/**
 * 
 */
package com.sp.sms.cmpp.exception;

/**
 * @author sunpeng
 * 
 */
public class CMPPRequestException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2083433711397650788L;

	public CMPPRequestException(String msg, Throwable t) {
		super(msg, t);
	}
}
