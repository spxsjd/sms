/**
 * 
 */
package com.sp.sms.cmpp;

import java.io.IOException;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sp.sms.cmpp.message.ActiveTestMessage;
import com.sp.sms.socket.SocketInspector;

/**
 * @author sunpeng
 * 
 */
public class SocketMonitor implements SocketInspector {

	private static final Logger logger = LoggerFactory
			.getLogger(SocketMonitor.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sp.sms.socket.inspection.SocketInspector#inspect(java.net.Socket)
	 */
	@Override
	public void inspect(Socket socket) throws IOException {
		logger.debug("Monitor socket......");
		ActiveTestMessage message = new ActiveTestMessage();
		CMPPMutualUtil.send(socket, message, null);

	}

}
