/**
 * 
 */
package com.sp.sms.cmpp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.sp.sms.cmpp.message.Header;

/**
 * @author sunpeng
 * 
 */
public class CMPPMutualUtil {

	public static <S extends Header, R extends Header> void send(Socket socket,
			S sended, R received) throws IOException {
		DataOutputStream out = null;
		DataInputStream in = null;
		try {
			out = new DataOutputStream(socket.getOutputStream());
			out.write(sended.getData());
			out.flush();
			if (received != null) {
				in = new DataInputStream(socket.getInputStream());
				received.setTotalLength(in.readInt());
				received.setCommandId(in.readInt());
				received.setSequenceId(in.readInt());
				received.readBody(in);
			}
		} finally {
//			if (out != null) {
//				out.close();
//			}
//			if (in != null) {
//				in.close();
//			}
		}
	}

}
