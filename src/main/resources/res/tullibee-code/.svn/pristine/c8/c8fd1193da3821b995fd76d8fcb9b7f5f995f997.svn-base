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
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.resource.spi.work.Work;

class Delivery<M, L extends MessageListener<M>> implements Work {

	private final M message;
	private final MessageEndpointFactory messageEndpointFactory;
	private final LogWriterFactory logWriterFactory;

	private volatile boolean released;

	public Delivery(final M message,
			final MessageEndpointFactory messageEndpointFactory,
			final LogWriterFactory logWriterFactory) {
		this.message = message;
		this.messageEndpointFactory = messageEndpointFactory;
		this.logWriterFactory = logWriterFactory;
	}

	public void release() {
		released = true;
	}

	@SuppressWarnings("unchecked")
	public void run() {
		if (!released) {
			try {
				((L) messageEndpointFactory.createEndpoint(null))
						.onMessage(message);
			} catch (final Exception e) {
				logFailure(e);
			}
		}
	}

	public void logFailure(final Exception exception) {
		try {
			final PrintWriter logWriter = logWriterFactory.getLogWriter();

			if (logWriter != null) {
				logWriter.format(
						"Failed to send message [%s]; exception to follow.",
						message);
				exception.printStackTrace(logWriter);
			}
		} catch (final ResourceException e) {
		}
	}
}
