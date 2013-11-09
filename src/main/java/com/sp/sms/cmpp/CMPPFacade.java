/**
 * 
 */
package com.sp.sms.cmpp;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sp.sms.DeliveryStation;
import com.sp.sms.cmpp.exception.CMPPRequestException;
import com.sp.sms.cmpp.message.Header;
import com.sp.sms.cmpp.message.SubmitMessage;
import com.sp.sms.socket.SocketSource;

/**
 * @author sunpeng
 * 
 */
public class CMPPFacade implements DeliveryStation {

	private static final Logger logger = LoggerFactory
			.getLogger(CMPPFacade.class);
	private SocketSource _socketSource;

	public CMPPFacade(Properties properties) {
		logger.info("Initialize the cmpp config......");
		CMPPConfig config = CMPPConfig.getInstance();
		config.setUser(properties.getProperty("sms.cmpp.user"));
		config.setSpId(properties.getProperty("sms.cmpp.sp_id"));
		config.setSecret(properties.getProperty("sms.cmpp.secret"));
		config.setVersion(Byte.parseByte(properties
				.getProperty("sms.cmpp.version")));
		config.setTpPid(Byte.parseByte(properties
				.getProperty("sms.cmpp.tp_pid")));
		config.setTpUdhi(Byte.parseByte(properties
				.getProperty("sms.cmpp.tp_udhi")));
		config.setFeeCode(properties.getProperty("sms.cmpp.fee_code"));
		config.setServiceId(properties.getProperty("sms.cmpp.service_id"));
		config.setRegisteredDelivery(Byte.parseByte(properties
				.getProperty("sms.cmpp.registered_delivery")));
	}

	@Override
	public void deliver(String content, String... targets) {
		this.submitMessage(content, MessageFormater.Format.Chinese, null, targets);
	}

	public void shutdown() {
		try {
			this._socketSource.close();
		} catch (SocketException e) {
			logger.error("Close the SocketSource error.", e);
		}
	}

	public  <R extends Header> void submitMessage(String msgContent, MessageFormater.Format format, R receiver,
			String... destTerminalId) {
		if (logger.isDebugEnabled()) {
			String dests = Arrays.toString(destTerminalId);
			logger.debug("Submit message[" + msgContent + "] to user " + dests
					+ ".");
		}
		List<String> subMessages = MessageFormater.splitMessage(msgContent);
		Socket socket = null;
		try {
			socket = _socketSource.getSocket();
			Integer i, size = subMessages.size();
			SubmitMessage message;
			byte[] content;
			for (i = 0; i < size; i++) {
				message = new SubmitMessage();
				content = format.toBytes(subMessages.get(i));
				message.setMsgContent(content);
				message.setDestTerminalId(destTerminalId);
				message.setMsgFmt(format.getCmppCode());
				message.setMsgLength(new Integer(content.length).byteValue());
				message.setPkTotal(size.byteValue());
				message.setPkNumber(new Integer(i + 1).byteValue());
				
				//logger.info("The send message:"+message.getData());
				CMPPMutualUtil.send(socket, message, receiver);
			}
		} catch (Exception e) {
			throw new CMPPRequestException("Submit message error.", e);
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}

	}
	
	

	public void setSocketSource(SocketSource socketSource) {
		this._socketSource = socketSource;
	}

}
