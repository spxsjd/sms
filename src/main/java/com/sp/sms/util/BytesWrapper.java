/**
 * 
 */
package com.sp.sms.util;

import java.io.UnsupportedEncodingException;

/**
 * @author sunpeng
 * 
 */
public class BytesWrapper {

	private byte[] container;
	private int length;
	private int increasedLength;

	public BytesWrapper(int initLength) {
		this.container = new byte[initLength];
		this.length = 0;
		this.increasedLength = initLength / 2;
	}

	public BytesWrapper(int initLength, int increasedLength) {
		this.container = new byte[initLength];
		this.length = 0;
		this.increasedLength = increasedLength;
	}

	public void append(byte[] bytes) {
		try {
			System.arraycopy(bytes, 0, this.container, this.length,
					bytes.length);
			this.length += bytes.length;
		} catch (ArrayIndexOutOfBoundsException e) {
			adjustContainer();
			append(bytes);
		}
	}

	public void append(byte value) {
		try {
			this.container[this.length] = value;
			// System.arraycopy(value, 0, this.container, this.length, 1);
			this.length += 1;
		} catch (ArrayIndexOutOfBoundsException e) {
			adjustContainer();
			append(value);
		}
	}

	public void append(String value) {
		append(value.getBytes());
	}

	public void append(Integer value) {
		append(BytesConvertor.int2byte(value));
	}

	public void blank(int length) {
		this.length += length;
		if (this.length > this.container.length) {
			adjustContainer();
		}
	}

	public void insert(byte[] bytes, int pos) {
		try {
			int addLength = bytes.length, restLength = this.length - pos, appendLength = addLength
					+ restLength;
			byte[] postfix = new byte[appendLength];
			System.arraycopy(bytes, 0, postfix, 0, addLength);
			System.arraycopy(this.container, pos, postfix, addLength,
					restLength);
			System.arraycopy(postfix, 0, this.container, pos, appendLength);
			this.length += addLength;
		} catch (ArrayIndexOutOfBoundsException e) {
			adjustContainer();
			insert(bytes, pos);
		}
	}

	public void insert(String value, int pos) {
		insert(value.getBytes(), pos);
	}

	public void insert(Integer value, int pos) {
		insert(BytesConvertor.int2byte(value), pos);
	}

	public byte[] getData() {
		byte[] data = new byte[this.length];
		System.arraycopy(this.container, 0, data, 0, this.length);
		return data;
	}

	public int getLength() {
		return this.length;
	}

	protected void adjustContainer() {
		byte[] newContainer = new byte[this.container.length
				+ this.increasedLength];
		System.arraycopy(this.container, 0, newContainer, 0,
				this.container.length);
		this.container = newContainer;
	}

	public static void main(String[] args) throws UnsupportedEncodingException {

		BytesWrapper wrapper = new BytesWrapper(10);
		
		wrapper.append(new Integer(8).byteValue());
		System.out.println("====" + wrapper.getLength());
		System.out.println("====" + new String(wrapper.getData()));
		wrapper.append("1111111111111111111".getBytes());
		wrapper.blank(3);
		wrapper.append("222".getBytes());
		wrapper.insert("3333333333".getBytes(), 0);
		wrapper.append("fff".getBytes()[0]);
		System.out.println("====" + new String(wrapper.getData()));
		System.out.println("====" + wrapper.getLength());

		byte[] old = "abcded".getBytes();
		System.arraycopy("xyx".getBytes(), 0, old, 2, 3);
		System.out.println("====" + new String(old));
		System.out.println("====" + Integer.toHexString(0x00000004));
		System.out.println("====" + Integer.valueOf("9f", 16));
	}

}
