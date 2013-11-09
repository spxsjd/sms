/**
 * 
 */
package com.sp.sms.socket;

import java.net.Socket;
import java.net.SocketException;

import org.apache.commons.pool.impl.GenericObjectPool;

/**
 * @author sunpeng
 * 
 */
public class SocketSource {

	/**
	 * 相关的配置和监测连接参数
	 * 
	 * @return
	 */

	/**
	 * The object pool that internally manages our connections.
	 */
	protected volatile GenericObjectPool<Socket> _socketPool = null;
	protected SocketConfig _config = null;
	protected SocketInspector _monitor;
	protected SocketInspector _initializer;

	public Socket getSocket() throws Exception {
		return (Socket) this.createSocketPool().borrowObject();
	}

	protected synchronized GenericObjectPool<Socket> createSocketPool()
			throws SocketException {
		if (closed) {
			throw new SocketException("Socket source is closed");
		}

		// Return the pool if we have already created it
		if (_socketPool != null) {
			return (_socketPool);
		}
		// Create an object pool to contain our active connections
		PoolableSocketFactory factory = new PoolableSocketFactory();
		this._socketPool = new GenericObjectPool<Socket>(factory);
		factory.init(this._config, this._socketPool, this._monitor,
				this._initializer);

		this._socketPool.setMaxActive(maxActive);
		this._socketPool.setMaxIdle(maxIdle);
		this._socketPool.setMinIdle(minIdle);
		this._socketPool.setMaxWait(maxWait);
		// gop.setTestOnBorrow(testOnBorrow);
		// gop.setTestOnReturn(testOnReturn);
		// gop.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
		// gop.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
		// gop.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		// gop.setTestWhileIdle(testWhileIdle);
		return this._socketPool;
	}

	public SocketConfig getConfig() {
		return _config;
	}

	public void setConfig(SocketConfig config) {
		this._config = config;
	}

	public SocketInspector getMonitor() {
		return _monitor;
	}

	public void setMonitor(SocketInspector monitor) {
		this._monitor = monitor;
	}

	public SocketInspector getInitializer() {
		return _initializer;
	}

	public void setInitializer(SocketInspector initializer) {
		this._initializer = initializer;
	}

	/**
	 * The maximum number of active connections that can be allocated from this
	 * pool at the same time, or negative for no limit.
	 */
	protected int maxActive = 1;

	/**
	 * <p>
	 * Returns the maximum number of active connections that can be allocated at
	 * the same time.
	 * </p>
	 * <p>
	 * A negative number means that there is no limit.
	 * </p>
	 * 
	 * @return the maximum number of active connections
	 */
	public synchronized int getMaxActive() {
		return this.maxActive;
	}

	/**
	 * Sets the maximum number of active connections that can be allocated at
	 * the same time. Use a negative value for no limit.
	 * 
	 * @param maxActive
	 *            the new value for maxActive
	 * @see #getMaxActive()
	 */
	public synchronized void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
		if (_socketPool != null) {
			_socketPool.setMaxActive(maxActive);
		}
	}

	/**
	 * The maximum number of connections that can remain idle in the pool,
	 * without extra ones being released, or negative for no limit. If maxIdle
	 * is set too low on heavily loaded systems it is possible you will see
	 * connections being closed and almost immediately new connections being
	 * opened. This is a result of the active threads momentarily closing
	 * connections faster than they are opening them, causing the number of idle
	 * connections to rise above maxIdle. The best value for maxIdle for heavily
	 * loaded system will vary but the default is a good starting point.
	 */
	protected int maxIdle = 1;

	/**
	 * <p>
	 * Returns the maximum number of connections that can remain idle in the
	 * pool.
	 * </p>
	 * <p>
	 * A negative value indicates that there is no limit
	 * </p>
	 * 
	 * @return the maximum number of idle connections
	 */
	public synchronized int getMaxIdle() {
		return this.maxIdle;
	}

	/**
	 * Sets the maximum number of connections that can remain idle in the pool.
	 * 
	 * @see #getMaxIdle()
	 * @param maxIdle
	 *            the new value for maxIdle
	 */
	public synchronized void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
		if (_socketPool != null) {
			_socketPool.setMaxIdle(maxIdle);
		}
	}

	/**
	 * The minimum number of active connections that can remain idle in the
	 * pool, without extra ones being created, or 0 to create none.
	 */
	protected int minIdle = 1;

	/**
	 * Returns the minimum number of idle connections in the pool
	 * 
	 * @return the minimum number of idle connections
	 * @see GenericObjectPool#getMinIdle()
	 */
	public synchronized int getMinIdle() {
		return this.minIdle;
	}

	/**
	 * Sets the minimum number of idle connections in the pool.
	 * 
	 * @param minIdle
	 *            the new value for minIdle
	 * @see GenericObjectPool#setMinIdle(int)
	 */
	public synchronized void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
		if (_socketPool != null) {
			_socketPool.setMinIdle(minIdle);
		}
	}

	/**
	 * The maximum number of milliseconds that the pool will wait (when there
	 * are no available connections) for a connection to be returned before
	 * throwing an exception, or <= 0 to wait indefinitely.
	 */
	protected long maxWait = 0;

	/**
	 * <p>
	 * Returns the maximum number of milliseconds that the pool will wait for a
	 * connection to be returned before throwing an exception.
	 * </p>
	 * <p>
	 * A value less than or equal to zero means the pool is set to wait
	 * indefinitely.
	 * </p>
	 * 
	 * @return the maxWait property value
	 */
	public synchronized long getMaxWait() {
		return this.maxWait;
	}

	/**
	 * <p>
	 * Sets the maxWait property.
	 * </p>
	 * <p>
	 * Use -1 to make the pool wait indefinitely.
	 * </p>
	 * 
	 * @param maxWait
	 *            the new value for maxWait
	 * @see #getMaxWait()
	 */
	public synchronized void setMaxWait(long maxWait) {
		this.maxWait = maxWait;
		if (_socketPool != null) {
			_socketPool.setMaxWait(maxWait);
		}
	}

	protected boolean closed;

	public synchronized void close() throws SocketException {
		closed = true;
		try {
			if (_socketPool != null) {
				_socketPool.close();
			}
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw (SocketException) new SocketException(
					"Cannot close connection pool").initCause(e);
		}
	}

	public synchronized boolean isClosed() {
		return closed;
	}

}
