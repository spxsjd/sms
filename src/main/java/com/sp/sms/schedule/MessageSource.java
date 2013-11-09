/**
 * 
 */
package com.sp.sms.schedule;

/**
 * @author sunpeng
 * 
 */
public interface MessageSource {

	public ShortMessage get();

	public void close();
}
