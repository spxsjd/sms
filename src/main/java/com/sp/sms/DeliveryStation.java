/**
 * 
 */
package com.sp.sms;

/**
 * @author sunpeng
 * 
 */
public interface DeliveryStation {

	public void deliver(String content, String... targets);
	
	public void shutdown();
}
