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

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ManagedConnectionFactory;

@SuppressWarnings("serial")
public class ConnectionFactoryImpl implements ConnectionFactory {

	private final ConnectionManager connectionManager;
	private final ManagedConnectionFactory managedConnectionFactory;
	private Reference reference;

	public ConnectionFactoryImpl(final ConnectionManager connectionManager,
			final ManagedConnectionFactory managedConnectionFactory) {
		this.connectionManager = connectionManager;
		this.managedConnectionFactory = managedConnectionFactory;
	}

	public Connection getConnection() throws ResourceException {
		return (Connection) connectionManager.allocateConnection(
				managedConnectionFactory, null);
	}

	public void setReference(final Reference arg0) {
		reference = arg0;
	}

	public Reference getReference() throws NamingException {
		if (reference == null) {
			throw new NamingException(
					format(
							"getReference called on a Connection with a null referance. [%s]",
							toString()));
		}

		return reference;
	}
}
