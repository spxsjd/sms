/**
 * 
 */
package com.sp.sms.cmpp;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sp.sms.cmpp.message.SubmitMessage;
import com.sp.sms.cmpp.message.SubmitMessageResp;
import com.sp.sms.socket.SocketConfig;
import com.sp.sms.socket.SocketSource;

/**
 * @author sunpeng
 * 
 */
public class CMPPMain {

	private static final Logger logger = LoggerFactory
			.getLogger(CMPPMain.class);
	protected CMPPFacade facade;

	public void setUp() throws Exception {
		String configFile = System.getProperty("cmpp.config");
		Properties p = new Properties();
		InputStream inStream = (configFile == null ? this.getClass()
				.getResourceAsStream("/cmpp.properties") : new FileInputStream(
				configFile));

		InputStreamReader reader = new InputStreamReader(inStream, "UTF-8");
		p.load(reader);

		SocketConfig socketConfig = new SocketConfig();
		socketConfig.setHost(p.getProperty("sms.cmpp.host"));
		socketConfig.setPort(Integer.parseInt(p.getProperty("sms.cmpp.port")));

		SocketInitializer initializer = new SocketInitializer();
		SocketMonitor monitor = new SocketMonitor();
		SocketSource socketSource = new SocketSource();
		socketSource.setConfig(socketConfig);
		socketSource.setInitializer(initializer);
		socketSource.setMonitor(monitor);
		socketSource.setMaxActive(1);
		socketSource.setMaxIdle(1);
		socketSource.setMaxWait(0);
		socketSource.setMinIdle(1);

		facade = new CMPPFacade(p);
		facade.setSocketSource(socketSource);
	}

	public static void main(String[] args) throws Exception {
		logger.info("Starting sms main.");
		CMPPMain main = new CMPPMain();
		main.setUp();
		SubmitMessageResp receiver = new SubmitMessageResp();
		main.facade.submitMessage(args[1], MessageFormater.Format.Chinese,
				receiver, args[0]);
		logger.info("Submit message finish.");
		
		logger.info("The received check =>" + receiver.getCommandId().equals(SubmitMessage.CMPP_SUBMIT_RESP));
		logger.info("The received message: cid=>" + receiver.getCommandId()
				+ ",sid=>" + receiver.getSequenceId() + ",length=>"
				+ receiver.getTotalLength());
		logger.info("The received message body: mid=>"+receiver.getMessageId()+",result=>"+receiver.getResult());
		main.facade.shutdown();

	}

}
