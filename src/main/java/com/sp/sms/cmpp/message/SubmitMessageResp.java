/**
 * 
 */
package com.sp.sms.cmpp.message;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author sunpeng
 * 
 */
public class SubmitMessageResp extends Header {

	private Long messageId;
	private byte result;

	public void readBody(DataInputStream in) throws IOException {
		this.messageId = in.readLong();
		this.result = in.readByte();

	}

	public Long getMessageId() {
		return messageId;
	}

	public byte getResult() {
		return result;
	}

}
