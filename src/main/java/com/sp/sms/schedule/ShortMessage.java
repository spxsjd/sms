/**
 * 
 */
package com.sp.sms.schedule;

/**
 * @author sunpeng
 * 
 */
public class ShortMessage {

	private String[] targets;
	private String content;

	public String[] getTargets() {
		return targets;
	}

	public String getContent() {
		return content;
	}

	public void setTargets(String[] targets) {
		this.targets = targets;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
