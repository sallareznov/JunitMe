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

import java.io.PrintWriter;

import javax.resource.ResourceException;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.work.Work;

import com.ib.client.EReader;

class ReaderWork implements Work {

	private final EReader reader;
	private final ManagedConnection managedConnection;

	public ReaderWork(final EReader reader,
			final ManagedConnection managedConnection) {
		this.reader = reader;
		this.managedConnection = managedConnection;
	}

	public void release() {
		reader.stop();
	}

	public void run() {
		try {
			reader.run();
		} finally {
			try {
				managedConnection.destroy();
			} catch (final ResourceException e) {
				logDestroyError(e);
			}
		}
	}

	private void logDestroyError(final ResourceException e) {
		try {
			final PrintWriter logWriter = managedConnection.getLogWriter();

			if (logWriter != null) {
				synchronized (logWriter) {
					logWriter
							.println("An error occurred while destroying the managed connection.");
					e.printStackTrace(logWriter);
				}
			}
		} catch (final Exception ex) {
		}
	}
}
