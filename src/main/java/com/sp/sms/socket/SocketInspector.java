/**
 * 
 */
package com.sp.sms.socket;

import java.io.IOException;
import java.net.Socket;

/**
 * @author sunpeng
 * 
 */
public interface SocketInspector {

	public void inspect(Socket socket) throws IOException;

}
