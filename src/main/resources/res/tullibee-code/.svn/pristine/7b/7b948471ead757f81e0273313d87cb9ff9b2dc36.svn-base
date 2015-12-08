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

import javax.resource.spi.ActivationSpec;
import javax.resource.spi.endpoint.MessageEndpointFactory;

class MessageEndpointFactoryKeyImpl implements MessageEndpointFactoryKey {
	private final ActivationSpec activationSpec;
	private final MessageEndpointFactory messageEndpointFactory;

	/**
	 * @param activationSpec
	 * @param messageEndpointFactory
	 */
	MessageEndpointFactoryKeyImpl(final ActivationSpec activationSpec,
			final MessageEndpointFactory messageEndpointFactory) {
		this.activationSpec = activationSpec;
		this.messageEndpointFactory = messageEndpointFactory;
	}

	/**
	 * Provides read access to the {@code activationSpec} property.
	 * 
	 * @return activationSpec
	 */
	public ActivationSpec getActivationSpec() {
		return activationSpec;
	}

	/**
	 * Provides read access to the {@code messageEndpointFactory} property.
	 * 
	 * @return messageEndpointFactory
	 */
	public MessageEndpointFactory getMessageEndpointFactory() {
		return messageEndpointFactory;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (activationSpec == null ? 0 : activationSpec.hashCode());
		result = prime
				* result
				+ (messageEndpointFactory == null ? 0 : messageEndpointFactory
						.hashCode());
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
		final MessageEndpointFactoryKeyImpl other = (MessageEndpointFactoryKeyImpl) obj;
		if (activationSpec == null) {
			if (other.activationSpec != null) {
				return false;
			}
		} else if (!activationSpec.equals(other.activationSpec)) {
			return false;
		}
		if (messageEndpointFactory == null) {
			if (other.messageEndpointFactory != null) {
				return false;
			}
		} else if (!messageEndpointFactory.equals(other.messageEndpointFactory)) {
			return false;
		}
		return true;
	}
}
