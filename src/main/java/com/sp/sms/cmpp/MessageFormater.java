/**
 * 
 */
package com.sp.sms.cmpp;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sunpeng
 * 
 */
public class MessageFormater {

	private static final int step = 58;
	private static final int max = 70;

	public enum Format {
		Ascii(0), Write2Card(3), Binary(4), Ucs2(8) {
			public byte[] toBytes(String content)
					throws UnsupportedEncodingException {
				return content.getBytes("iso-10646-ucs-2");
			}
		},
		Chinese(15) {
			public byte[] toBytes(String content)
					throws UnsupportedEncodingException {
				return content.getBytes("gb2312");
			}
		};

		private byte cmppCode;

		private Format(Integer cmppCode) {
			this.cmppCode = cmppCode.byteValue();
		}

		public byte[] toBytes(String content)
				throws UnsupportedEncodingException {
			return content.getBytes("iso-8859-1");
		}

		public byte getCmppCode() {
			return cmppCode;
		}

	}

	public static List<String> splitMessage(String msg) {
		if (msg == null) {
			return null;
		}
		List<String> v = new ArrayList<String>();
		if (msg.length() <= max) {
			v.add(msg);
			return v;
		} else {
			int length = msg.length();
			int splitNum = (int) (length / step) + 1;
			boolean flag = true;
			if (splitNum > 10) {
				splitNum = 10;
				flag = false;
			}
			int len = 0;
			for (int i = 0; i < splitNum; i++) {
				if (i == 0) {
					String spMsg = msg.substring(0, step)
							+ getFirstEnd(splitNum);
					v.add(spMsg);
					spMsg = null;
					len = step;
					continue;
				}
				if (i == splitNum - 1) {
					String spMsg = null;
					if (flag == false)
						spMsg = getSecondFirst(i, splitNum)
								+ msg.substring(len, len + step);
					v.add(spMsg);
					spMsg = null;
					continue;
				}
				String spMsg = null;
				spMsg = getSecondFirst(i, splitNum)
						+ msg.substring(len, len + step)
						+ getSecondEnd(i, splitNum);
				v.add(spMsg);
				spMsg = null;
			}
			return v;
		}
	}

	private static String getFirstEnd(int spNum) {
		return "(1/" + spNum + ")";
	}

	private static String getSecondFirst(int n, int spNum) {
		return "(½Ó" + n + "/" + spNum + ")";
	}

	private static String getSecondEnd(int n, int spNum) {
		return "(" + (n + 1) + "/" + spNum + ")";
	}
}
