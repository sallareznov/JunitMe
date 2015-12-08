// Copyright 2010-2012 Christopher Redekop
// 
// This file is part of the Tullibee Resource Adapter.  The Tullibee Resource
// Adapter provides connectivity to Interactive Brokers through the Tullibee
// API, in accordance with the Java EE Connector Architecture.
// 
// The Tullibee Resource Adapter is free software: you can redistribute it
// and/or modify it under the terms of the GNU Lesser General Public License
// as published by the Free Software Foundation, either version 3 of the
// License, or (at your option) any later version.
// 
// The Tullibee Resource Adapter is distributed in the hope that it will be
// useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
// General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License
// along with the Tullibee Resource Adapter.  If not, see
// <http://www.gnu.org/licenses/>.

package net.sf.tullibee.resource;

import static java.lang.String.format;
import static javax.resource.spi.ConnectionEvent.CONNECTION_CLOSED;
import static javax.resource.spi.ConnectionEvent.CONNECTION_ERROR_OCCURRED;
import static net.sf.tullibee.resource.CommonMessages.NO_LOCAL_TRANSACTION_SUPPORT;
import static net.sf.tullibee.resource.CommonMetaData.EIS_PRODUCT_NAME;
import static net.sf.tullibee.resource.CommonMetaData.EIS_PRODUCT_VERSION;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.resource.NotSupportedException;
import javax.resource.ResourceException;
import javax.resource.spi.CommException;
import javax.resource.spi.ConnectionEvent;
import javax.resource.spi.ConnectionEventListener;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.EISSystemException;
import javax.resource.spi.LocalTransaction;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionMetaData;
import javax.resource.spi.ResourceAdapterInternalException;
import javax.security.auth.Subject;
import javax.transaction.xa.XAResource;

import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import com.ib.client.EException;

public class ManagedConnectionImpl implements ManagedConnection,
		LogWriterFactory {
	private final static ManagedConnectionMetaData MANAGED_CONNECTION_META_DATA = new ManagedConnectionMetaData() {
		public String getUserName() throws ResourceException {
			return "";
		}

		public int getMaxConnections() throws ResourceException {
			return 8;
		}

		public String getEISProductVersion() throws ResourceException {
			return EIS_PRODUCT_VERSION;
		}

		public String getEISProductName() throws ResourceException {
			return EIS_PRODUCT_NAME;
		}
	};

	private final static byte[] ASSOCIATION_MUTEX = new byte[0];

	private final ConnectionId connectionId;
	private final List<ConnectionImpl> connectionImpls = new ArrayList<ConnectionImpl>();
	private final List<ConnectionEventListener> connectionEventListeners = new ArrayList<ConnectionEventListener>();
	private volatile PrintWriter logWriter;
	private int tickertId;

	private final EClientSocket clientSocket;

	public ManagedConnectionImpl(final ConnectionId id,
			final EClientSocket clientSocket) {
		this.connectionId = id;
		this.clientSocket = clientSocket;
	}

	public void addConnectionEventListener(
			final ConnectionEventListener listener) {
		synchronized (connectionEventListeners) {
			connectionEventListeners.add(listener);
		}
	}

	public void associateConnection(final Object connection)
			throws ResourceException {
		synchronized (ASSOCIATION_MUTEX) {
			final ConnectionImpl connectionImpl = (ConnectionImpl) connection;
			final ManagedConnectionImpl managedConnectionImpl = connectionImpl
					.getManagedConnectionImpl();

			if (managedConnectionImpl != null) {
				managedConnectionImpl.connectionImpls.remove(connectionImpl);
			}

			try {
				connectionImpl.setManagedConnectionImpl(this);
				connectionImpls.add(connectionImpl);
			} catch (final IllegalStateException e) {
				if (logWriter != null) {
					synchronized (logWriter) {
						e.printStackTrace(logWriter);
					}
				}
			}
		}
	}

	public void cleanup() throws ResourceException {
		invalidateConnections();
	}

	private void invalidateConnections() {
		synchronized (ASSOCIATION_MUTEX) {
			for (final ConnectionImpl connectionImpl : connectionImpls) {
				if (connectionImpl != null) {
					connectionImpl.invalidate();
				}
			}

			connectionImpls.clear();
		}
	}

	public void destroy() throws ResourceException {
		try {
			invalidateConnections();
			
			clientSocket.eDisconnect();
		} catch (final Exception e) {
			throw new EISSystemException(e);
		}
	}

	public Object getConnection(final Subject subject,
			final ConnectionRequestInfo cxRequestInfo) throws ResourceException {
		final ConnectionImpl connectionImpl = new ConnectionImpl(this);

		synchronized (ASSOCIATION_MUTEX) {
			connectionImpls.add(connectionImpl);
		}

		return connectionImpl;
	}

	public LocalTransaction getLocalTransaction() throws ResourceException {
		throw new NotSupportedException(NO_LOCAL_TRANSACTION_SUPPORT);
	}

	public ManagedConnectionMetaData getMetaData() throws ResourceException {
		return MANAGED_CONNECTION_META_DATA;
	}

	public XAResource getXAResource() throws ResourceException {
		throw new NotSupportedException("XA transactions are not supported.");
	}

	public void removeConnectionEventListener(
			final ConnectionEventListener listener) {
		synchronized (connectionEventListeners) {
			connectionEventListeners.remove(listener);
		}
	}

	public PrintWriter getLogWriter() throws ResourceException {
		return logWriter;
	}

	public void setLogWriter(final PrintWriter out) throws ResourceException {
		logWriter = out;
	}

	private void fireConnectionClosed(final Object connection) {
		final ConnectionEvent connectionEvent = new ConnectionEvent(this,
				CONNECTION_CLOSED);
		connectionEvent.setConnectionHandle(connection);

		fireConnectionEvent(connectionEvent);
	}

	void fireConnectionErrorOccured(Exception exception) {
		fireConnectionEvent(new ConnectionEvent(this,
				CONNECTION_ERROR_OCCURRED, exception));
	}

	private void fireConnectionEvent(final ConnectionEvent connectionEvent) {
		synchronized (connectionEventListeners) {
			for (final ConnectionEventListener connectionEventListener : connectionEventListeners) {
				connectionEventListener.connectionClosed(connectionEvent);
			}
		}
	}

	void close(final ConnectionImpl connectionImpl) {
		synchronized (ASSOCIATION_MUTEX) {
			connectionImpls.remove(connectionImpl);
		}

		fireConnectionClosed(connectionImpl);
	}

	public void requestCurrentTime() throws ResourceException {
		try {
			clientSocket.reqCurrentTime();
		} catch (Exception e) {
			handleException(e);
		}
	}

	private void handleException(Exception e) throws ResourceException {
		try {
			throw e;
		} catch (IOException ex) {
			fireAndThrow(ex, new CommException(ex));
		} catch (EException ex) {
			fireAndThrow(ex, new EISSystemException(e));
		} catch (Exception ex) {
			fireAndThrow(ex, new ResourceAdapterInternalException(ex));
		}
	}

	private void fireAndThrow(Exception cause, ResourceException resourceException) throws ResourceException {
		fireConnectionErrorOccured(cause);
		
		throw resourceException;
	}

	public int getServerVersion() throws ResourceException {
		int serverVersion = 0;
		
		try {
			serverVersion = clientSocket.serverVersion();
		} catch (EException e) {
			fireAndThrow(e, new EISSystemException(e));
		}
		
		return serverVersion;
	}

	public String getConnectionTime() throws ResourceException {
		String twsConnectionTime = null;
		
		try {
			twsConnectionTime = clientSocket.TwsConnectionTime();
		} catch (EException e) {
			fireAndThrow(e, new EISSystemException(e));
		}
		
		return twsConnectionTime;
	}

	public ConnectionId getConnectionId() {
		return connectionId;
	}
	
	private synchronized int getNextTickerId() {
		return tickertId++;
	}

	public void cancelRealTimeBars(RequestId requestId) throws ResourceException {
		if (!this.connectionId.equals(requestId.getConnectionId())) {
			throw new IllegalArgumentException(format("The specified request [%s] does not match this connection's ID [%s].", requestId, connectionId));
		}
		
		try {
			clientSocket.cancelRealTimeBars(requestId.getTickerId());
		} catch (Exception e) {
			handleException(e);
		}
	}

	public RequestId requestRealTimeBars(Contract contract,
			BarType barType, boolean useRegularTradingHours) throws ResourceException {
		int tickerId = getNextTickerId();
		
		try {
			clientSocket.reqRealTimeBars(tickerId, contract, 5, barType.name(),
					useRegularTradingHours);
		} catch (Exception e) {
			handleException(e);
		}
		
		return getRequestId(tickerId);
	}

	RequestId getRequestId(int tickerId) {
		return new RequestIdImpl(tickerId, connectionId);
	}
}
