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

import java.util.Date;

public class ConnectionIdImpl implements ConnectionId {
	private final String serverName;
	private final Integer portNumber;
	private final Integer clientId;
	private final Date date;

	ConnectionIdImpl(final String serverName, final Integer portNumber,
			final Integer clientId, final Date date) {
		this.serverName = serverName;
		this.portNumber = portNumber;
		this.clientId = clientId;
		this.date = date;
	}

	@Override
	public String getServerName() {
		return serverName;
	}

	@Override
	public Integer getPortNumber() {
		return portNumber;
	}

	@Override
	public Integer getClientId() {
		return clientId;
	}

	@Override
	public Date getDate() {
		return date;
	}

	@Override
	public String toString() {
		return format(
				"ConnectionIdImpl [clientId=%s, connectionDateTime=%s, portNumber=%s, serverName=%s]",
				clientId, date, portNumber, serverName);
	}
}
