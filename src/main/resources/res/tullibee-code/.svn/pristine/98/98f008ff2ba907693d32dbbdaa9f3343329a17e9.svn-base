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

import java.io.DataInputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import javax.resource.ResourceException;
import javax.resource.spi.ConnectionDefinition;
import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.EISSystemException;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionFactory;
import javax.resource.spi.ResourceAdapter;
import javax.resource.spi.ResourceAdapterAssociation;
import javax.resource.spi.work.Work;
import javax.security.auth.Subject;

import com.ib.client.EClientSocket;
import com.ib.client.EReader;
import com.ib.client.EWrapper;

@ConnectionDefinition(connectionFactory = ConnectionFactory.class, connectionFactoryImpl = ConnectionFactoryImpl.class, connection = Connection.class, connectionImpl = ConnectionImpl.class)
@SuppressWarnings("serial")
public class ManagedConnectionFactoryImpl implements ManagedConnectionFactory,
		ResourceAdapterAssociation {
	private static int clientId;

	private final DateFormat dateFormat = new SimpleDateFormat(
			"yyyyMMdd HH:mm:ss zzzz");

	private TullibeeResourceAdapter tullibeeResourceAdapter;
	private transient PrintWriter logWriter;

	public Object createConnectionFactory() throws ResourceException {
		return new ConnectionFactoryImpl(createConnectionManager(), this);
	}

	public Object createConnectionFactory(final ConnectionManager cxManager)
			throws ResourceException {
		return new ConnectionFactoryImpl(cxManager, this);
	}

	public ManagedConnection createManagedConnection(final Subject subject,
			final ConnectionRequestInfo cxRequestInfo) throws ResourceException {
		EClientSocket clientSocket = null;
		ManagedConnectionImpl managedConnectionImpl = null;

		try {
			final String serverName = tullibeeResourceAdapter.getServerName();
			final int portNumber = tullibeeResourceAdapter.getPortNumber();
			final int clientId = getNextClientId();

			clientSocket = createClientSocket();
			clientSocket.eConnect(serverName, portNumber, clientId);

			final ConnectionId id = createConnectionId(serverName, portNumber,
					clientId, clientSocket.TwsConnectionTime());

			managedConnectionImpl = createManagedConnectionImpl(id,
					clientSocket);

			final Work work = createWork(
					createReader(clientSocket.dataInputStream(),
							createWrapper(id, managedConnectionImpl),
							clientSocket.serverVersion()),
					managedConnectionImpl);

			tullibeeResourceAdapter.startWork(work);
		} catch (final ResourceException e) {
			abortCreateManagedConnection(managedConnectionImpl, clientSocket);
			throw e;
		} catch (final Exception e) {
			abortCreateManagedConnection(managedConnectionImpl, clientSocket);
			throw new EISSystemException(e);
		}

		return managedConnectionImpl;
	}

	private void abortCreateManagedConnection(
			final ManagedConnectionImpl managedConnectionImpl,
			final EClientSocket clientSocket) {
		try {
			if (managedConnectionImpl != null) {
				managedConnectionImpl.destroy();
			} else if (clientSocket != null) {
				clientSocket.eDisconnect();
			}
		} catch (final Exception e) {
			if (logWriter != null) {
				synchronized (logWriter) {
					logWriter
							.println("An error occurred while cleaning up after the creation of a managed connection failed.");

					e.printStackTrace(logWriter);
				}
			}
		}
	}

	public PrintWriter getLogWriter() throws ResourceException {
		return logWriter;
	}

	@SuppressWarnings("rawtypes")
	public ManagedConnection matchManagedConnections(final Set connectionSet,
			final Subject subject, final ConnectionRequestInfo cxRequestInfo)
			throws ResourceException {
		return (ManagedConnection) (connectionSet != null
				&& !connectionSet.isEmpty() ? connectionSet.iterator().next()
				: null);
	}

	public void setLogWriter(final PrintWriter out) throws ResourceException {
		logWriter = out;
	}

	public ResourceAdapter getResourceAdapter() {
		return tullibeeResourceAdapter;
	}

	public void setResourceAdapter(final ResourceAdapter ra)
			throws ResourceException {
		tullibeeResourceAdapter = (TullibeeResourceAdapter) ra;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ (tullibeeResourceAdapter == null ? 0
						: tullibeeResourceAdapter.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ManagedConnectionFactoryImpl other = (ManagedConnectionFactoryImpl) obj;
		if (tullibeeResourceAdapter == null) {
			if (other.tullibeeResourceAdapter != null) {
				return false;
			}
		} else if (!tullibeeResourceAdapter
				.equals(other.tullibeeResourceAdapter)) {
			return false;
		}

		return true;
	}

	private Integer getNextClientId() {
		int nextClientId;

		synchronized (ManagedConnectionFactoryImpl.class) {
			nextClientId = clientId++;
		}

		return nextClientId;
	}

	public EClientSocket createClientSocket() {
		return new EClientSocket();
	}

	public ConnectionId createConnectionId(final String serverName,
			final Integer portNumber, final Integer clientId,
			final String connectionDate) throws ParseException {
		Date date;

		synchronized (dateFormat) {
			date = dateFormat.parse(connectionDate);
		}

		return new ConnectionIdImpl(serverName, portNumber, clientId, date);
	}

	public ConnectionManager createConnectionManager() {
		return new ConnectionManagerImpl();
	}

	public ManagedConnectionImpl createManagedConnectionImpl(ConnectionId id,
			final EClientSocket clientSocket) {
		return new ManagedConnectionImpl(id, clientSocket);
	}

	public EReader createReader(final DataInputStream dataInputStream,
			final EWrapper wrapper, final int serverVersion) {
		return new EReader(dataInputStream, wrapper, serverVersion);
	}

	public Work createWork(final EReader reader,
			final ManagedConnection managedConnection) {
		return new ReaderWork(reader, managedConnection);
	}

	public EWrapper createWrapper(final ConnectionId connectionId,
			final ManagedConnectionImpl managedConnectionImpl) {
		return new WrapperImpl(connectionId, tullibeeResourceAdapter,
				DeliveryFactoryImpl.instance(), managedConnectionImpl);
	}
}
