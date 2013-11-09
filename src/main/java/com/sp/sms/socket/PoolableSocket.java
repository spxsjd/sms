/**
 * 
 */
package com.sp.sms.socket;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Connection;

import org.apache.commons.pool.ObjectPool;

/**
 * @author sunpeng
 * 
 */
public class PoolableSocket extends Socket {

	protected ObjectPool<Socket> _pool = null;

	protected SocketPacemaker _pacemaker;

	protected volatile boolean _closed = false;

	/**
	 * 默认的构造函数
	 */
	public PoolableSocket(ObjectPool<Socket> pool) {
		super();
		this._pool = pool;
	}

	public void setPacemaker(SocketPacemaker socketPacemaker) {
		this._pacemaker = socketPacemaker;
		this._pacemaker.prepare();
	}

	/**
	 * 当客户端关闭连接的时候状态设置为true(空闲）
	 */
	public void close() throws IOException {
		if (_closed) {
			return;
		}
		boolean isUnderlyingConectionClosed = false;
		try {
			isUnderlyingConectionClosed = super.isClosed();
		} catch (Exception e) {
			try {
				_pool.invalidateObject(this); // XXX should be guarded to happen
												// at most once
			} catch (IllegalStateException ise) {
				passivate();
				reallyClose();
			} catch (Exception ie) {
				// DO NOTHING the original exception will be rethrown
			}
			this.wrapException("Cannot close socket (isClosed check failed)", e);
		}

		if (!isUnderlyingConectionClosed) {
			// Normal close: underlying connection is still open, so we
			// simply need to return this proxy to the pool
			try {
				_pool.returnObject(this); // XXX should be guarded to happen at
											// most once
			} catch (IllegalStateException e) {
				// pool is closed, so close the connection
				passivate();
				reallyClose();
			} catch (RuntimeException e) {
				throw e;
			} catch (Exception e) {
				this.wrapException(
						"Cannot close socket (return to pool failed)", e);
			}
		} else {
			// Abnormal close: underlying connection closed unexpectedly, so we
			// must destroy this proxy
			try {
				_pool.invalidateObject(this); // XXX should be guarded to happen
												// at most once
			} catch (IllegalStateException e) {
				// pool is closed, so close the connection
				passivate();
				reallyClose();
			} catch (Exception ie) {
				// DO NOTHING, "Already closed" exception thrown below
			}
			throw new SocketException("Already closed.");
		}
	}

	protected void wrapException(String message, Throwable t)
			throws SocketException {
		SocketException e = new SocketException(message);
		throw (SocketException) e.initCause(t);
	}

	/**
	 * Actually close my underlying {@link Connection}.
	 */
	public void reallyClose() throws IOException {
		if (this._pacemaker != null) {
			this._pacemaker.pause();
		}
		super.close();
	}

	public void destroy() {
		try {
			this.reallyClose();
		} catch (IOException e) {
			// ignore
		}
	}

	protected void passivate() {
		_closed = true;
		if (this._pacemaker != null) {
			this._pacemaker.prepare();
		}
	}

	protected void activate() {
		_closed = false;
		if (this._pacemaker != null) {
			this._pacemaker.pause();
		}

	}
}
