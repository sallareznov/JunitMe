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

import java.util.Collection;
import java.util.concurrent.locks.Lock;

import javax.resource.spi.ActivationSpec;
import javax.resource.spi.endpoint.MessageEndpointFactory;

public interface MessageEndpointFactoryRegistry {

	/**
	 * @see java.util.concurrent.locks.ReadWriteLock#readLock()
	 */
	Lock readLock();

	/**
	 * @see java.util.concurrent.locks.ReadWriteLock#writeLock()
	 */
	Lock writeLock();

	MessageEndpointFactory registerMessageEndpointFactory(
			MessageEndpointFactory messageEndpointFactory,
			ActivationSpec activationSpec);

	MessageEndpointFactory unregisterMessageEndpointFactory(
			MessageEndpointFactory messageEndpointFactory,
			ActivationSpec activationSpec);

	Collection<MessageEndpointFactoryKey> getRegistrationKeys(
			Class<?> activationSpecClass);

	MessageEndpointFactory getRegisteredEndpointFactory(
			MessageEndpointFactoryKey messageEndpointFactoryKey);
}
