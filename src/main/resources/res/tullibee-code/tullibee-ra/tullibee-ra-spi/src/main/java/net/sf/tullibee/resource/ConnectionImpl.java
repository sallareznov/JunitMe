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

import javax.resource.ResourceException;
import javax.resource.spi.IllegalStateException;

import com.ib.client.Contract;

public class ConnectionImpl implements Connection {
	private static final String INVALID_CONNECTION = "Connection is invalid.";

	public ManagedConnectionImpl managedConnectionImpl;
	private boolean valid = true;

	ConnectionImpl(final ManagedConnectionImpl managedConnectionImpl) {
		this.managedConnectionImpl = managedConnectionImpl;
	}

	@Override
	public void close() throws ResourceException {
		checkValidity();

		managedConnectionImpl.close(this);
	}

	@Override
	public ConnectionId getConnectionId() throws ResourceException {
		checkValidity();
		
		return managedConnectionImpl.getConnectionId();
	}

	void checkValidity() throws ResourceException {
		if (!valid) {
			throw new IllegalStateException(INVALID_CONNECTION);
		}
	}

	ManagedConnectionImpl getManagedConnectionImpl() {
		checkValidityRuntime();

		return managedConnectionImpl;
	}

	void setManagedConnectionImpl(
			final ManagedConnectionImpl managedConnectionImpl) {
		checkValidityRuntime();

		this.managedConnectionImpl = managedConnectionImpl;
	}

	private void checkValidityRuntime() {
		if (!valid) {
			throw new java.lang.IllegalStateException(INVALID_CONNECTION);
		}
	}

	void invalidate() {
		managedConnectionImpl = null;
		valid = false;
	}

	public int getServerVersion() throws ResourceException {
		checkValidity();

		return managedConnectionImpl.getServerVersion();
	}

	public String getConnectionTime() throws ResourceException {
		checkValidity();

		return managedConnectionImpl.getConnectionTime();
	}

	public void requestCurrentTime() throws ResourceException {
		checkValidity();

		managedConnectionImpl.requestCurrentTime();
	}

	public void cancelRealTimeBars(RequestId requestId) throws ResourceException {
		checkValidity();
		
		managedConnectionImpl.cancelRealTimeBars(requestId);
	}

	public RequestId requestRealTimeBars(Contract contract, BarType barType,
			boolean useRegularTradingHours) throws ResourceException {
		checkValidity();
		
		return managedConnectionImpl.requestRealTimeBars(contract, barType,
				useRegularTradingHours);
	}
}
