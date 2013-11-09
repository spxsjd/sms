/**
 * 
 */
package com.sp.sms.cmpp.message;

import java.io.DataInputStream;
import java.io.IOException;

import com.sp.sms.cmpp.SequenceIndentifier;
import com.sp.sms.util.BytesWrapper;

/**
 * @author sunpeng
 * 
 */
public class Header {

	/**
	 * 消息总长度(含消息头及消息体)
	 */
	protected Integer totalLength;
	/**
	 * 命令或响应类型
	 */
	protected Integer commandId;
	/**
	 * 消息流水号,顺序累加,步长为1,循环使用
	 */
	protected Integer sequenceId;

	public Header() {

	}

	public Header(int commandId) {
		this.commandId = commandId;
		this.sequenceId = SequenceIndentifier.getInstance().nextSequence();
	}

	public byte[] getData() {

		BytesWrapper wrapper = new BytesWrapper(100);
		wrapper.append(this.commandId);
		wrapper.append(this.sequenceId);
		byte[] body = getBody();
		if (body != null && body.length > 0) {
			wrapper.append(body);
		}

		this.totalLength = wrapper.getLength() + 4;
		// System.out.println("======data length = "+this.totalLength);
		wrapper.insert(this.totalLength, 0);
		// System.out.println("======total length"+wrapper.getLength());
		// System.out.println("======total length"+wrapper.getData()[0]);
		// System.out.println("======total length"+wrapper.getData()[1]);
		// System.out.println("======total length"+wrapper.getData()[2]);
		// System.out.println("======total length"+wrapper.getData()[3]);
		return wrapper.getData();
	}

	public void readBody(DataInputStream in) throws IOException {

	}

	protected byte[] getBody() {
		return null;
	}

	public Integer getTotalLength() {
		return totalLength;
	}

	public void setTotalLength(Integer totalLength) {
		this.totalLength = totalLength;
	}

	public Integer getCommandId() {
		return commandId;
	}

	public void setCommandId(Integer commandId) {
		this.commandId = commandId;
	}

	public Integer getSequenceId() {
		return sequenceId;
	}

	public void setSequenceId(Integer sequenceId) {
		this.sequenceId = sequenceId;
	}

}
