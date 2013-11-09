/**
 * 
 */
package com.sp.sms.socket;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;

/**
 * @author sunpeng
 * 
 */
public class SocketConfig {

	private String host;
	private Integer port;
	private Integer timeout = 6000;
	private Integer soTimeout = 30000;
	private Long monitorInterval = 180000l;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public Integer getSoTimeout() {
		return soTimeout;
	}

	public Long getMonitorInterval() {
		return monitorInterval;
	}

	public void setSoTimeout(Integer soTimeout) {
		this.soTimeout = soTimeout;
	}

	public void setMonitorInterval(Long monitorInterval) {
		this.monitorInterval = monitorInterval;
	}

	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	public SocketAddress getSocketAddress() throws UnknownHostException {
		return new InetSocketAddress(this.host, this.port);
	}
}
