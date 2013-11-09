/**
 * 
 */
package com.sp.sms.cmpp;

import java.io.IOException;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sp.sms.cmpp.exception.CMPPRAuthenticationException;
import com.sp.sms.cmpp.message.ConnectMessage;
import com.sp.sms.cmpp.message.Header;
import com.sp.sms.socket.SocketInspector;

/**
 * @author sunpeng
 * 
 */
public class SocketInitializer implements SocketInspector {

	private static final Logger logger = LoggerFactory
			.getLogger(SocketInitializer.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sp.sms.socket.SocketInspector#inspect(java.net.Socket)
	 */
	@Override
	public void inspect(Socket socket) throws IOException {
		logger.info("Init socket......");
		ConnectMessage message = new ConnectMessage();
		Header receiver = new Header();
		CMPPMutualUtil.send(socket, message, receiver);
		if (!receiver.getCommandId().equals(
				ConnectMessage.COMMAND_CMPP_CONNECT_RESP)) {
			throw new CMPPRAuthenticationException("Auth the cmpp sp error.");
		} else {
			logger.info("Login cmpp successful......");
		}
	}

}
