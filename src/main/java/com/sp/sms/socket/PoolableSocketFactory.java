/**
 * 
 */
package com.sp.sms.socket;

import java.net.Socket;
import java.util.Timer;

import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.PoolableObjectFactory;

/**
 * @author sunpeng
 * 
 */
public class PoolableSocketFactory implements
		PoolableObjectFactory<Socket> {

	protected ObjectPool<Socket> _pool = null;
	protected SocketConfig _config = null;
	protected SocketInspector _monitor;
	protected SocketInspector _initializer;
	protected Timer _monitoringTimer;

	public void init(SocketConfig config, ObjectPool<Socket> pool) {
		this._config = config;
		this._pool = pool;
	}

	public void init(SocketConfig config, ObjectPool<Socket> pool,
			SocketInspector monitor) {
		init(config, pool);
		this._monitor = monitor;
		if (this._monitor != null && this._config.getMonitorInterval() != null) {
			_monitoringTimer = new Timer("Timer-Sockets", true);
		}
	}

	public void init(SocketConfig config, ObjectPool<Socket> pool,
			SocketInspector monitor, SocketInspector initializer) {
		init(config, pool, monitor);
		this._initializer = initializer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.commons.pool.PoolableObjectFactory#makeObject()
	 */
	@Override
	public Socket makeObject() throws Exception {
		PoolableSocket socket = new PoolableSocket(this._pool);
		socket.connect(this._config.getSocketAddress(),
				this._config.getTimeout());
		socket.setSoTimeout(this._config.getSoTimeout());
		// 初始化连接
		if (this._initializer != null) {
			this._initializer.inspect(socket);
		}
		if (this._monitoringTimer != null) {
			SocketPacemaker pacemaker = new SocketPacemaker(this, socket);
			socket.setPacemaker(pacemaker);
		}
		return socket;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.commons.pool.PoolableObjectFactory#destroyObject(java.lang
	 * .Object)
	 */
	@Override
	public void destroyObject(Socket obj) throws Exception {
		if (obj instanceof PoolableSocket) {
			((PoolableSocket) obj).reallyClose();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.commons.pool.PoolableObjectFactory#validateObject(java.lang
	 * .Object)
	 */
	@Override
	public boolean validateObject(Socket obj) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.commons.pool.PoolableObjectFactory#activateObject(java.lang
	 * .Object)
	 */
	@Override
	public void activateObject(Socket obj) throws Exception {
		if (obj instanceof PoolableSocket) {
			((PoolableSocket) obj).activate();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.commons.pool.PoolableObjectFactory#passivateObject(java.lang
	 * .Object)
	 */
	@Override
	public void passivateObject(Socket obj) throws Exception {
		if (obj instanceof PoolableSocket) {
			((PoolableSocket) obj).passivate();
		}
	}

}
