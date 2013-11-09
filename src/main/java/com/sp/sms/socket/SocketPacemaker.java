/**
 * 
 */
package com.sp.sms.socket;

import java.net.Socket;
import java.util.TimerTask;

/**
 * @author sunpeng
 * 
 */
public class SocketPacemaker extends TimerTask {

	protected PoolableSocketFactory _factory;
	protected Socket _socket;

	public SocketPacemaker(PoolableSocketFactory factory, Socket socket) {
		this._factory = factory;
		this._socket = socket;
	}

	public void prepare() {
		long interval = _factory._config.getMonitorInterval();
		_factory._monitoringTimer.schedule(this, interval, interval);
	}

	public void pause() {
		super.cancel();
		_factory._monitoringTimer.purge();
	}

	public void shutdown() {
		this.pause();
	}

	@Override
	public void run() {
		try {
			_factory._monitor.inspect(_socket);
		} catch (Exception e) {
			// log
			try {
				_factory._pool.invalidateObject(_socket);
			} catch (Exception e1) {
				// ignore
			}
		}
	}

}
