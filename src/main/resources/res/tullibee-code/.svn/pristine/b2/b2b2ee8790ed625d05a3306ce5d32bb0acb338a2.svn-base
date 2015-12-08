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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.resource.spi.ActivationSpec;
import javax.resource.spi.endpoint.MessageEndpointFactory;

class MessageEndpointFactoryRegistryImpl implements
		MessageEndpointFactoryRegistry {

	final Map<MessageEndpointFactoryKey, MessageEndpointFactory> messageEndpointFactories;
	final ReadWriteLock messageEndpointFactoryMapsLock;
	final MessageEndpointFactoryKeyFactory messageEndpointFactoryKeyFactory;

	MessageEndpointFactoryRegistryImpl() {
		this(new HashMap<MessageEndpointFactoryKey, MessageEndpointFactory>(),
				MessageEndpointFactoryKeyFactoryImpl.instance(),
				new ReentrantReadWriteLock());
	}

	MessageEndpointFactoryRegistryImpl(
			final Map<MessageEndpointFactoryKey, MessageEndpointFactory> messageEndpointFactories,
			final MessageEndpointFactoryKeyFactory messageEndpointFactoryKeyFactory,
			final ReadWriteLock messageEndpointFactoryMapsLock) {
		this.messageEndpointFactories = messageEndpointFactories;
		this.messageEndpointFactoryMapsLock = messageEndpointFactoryMapsLock;
		this.messageEndpointFactoryKeyFactory = messageEndpointFactoryKeyFactory;
	}

	/**
	 * @see java.util.concurrent.locks.ReadWriteLock#readLock()
	 */
	public Lock readLock() {
		return messageEndpointFactoryMapsLock.readLock();
	}

	/**
	 * @see java.util.concurrent.locks.ReadWriteLock#writeLock()
	 */
	public Lock writeLock() {
		return messageEndpointFactoryMapsLock.writeLock();
	}

	public MessageEndpointFactory registerMessageEndpointFactory(
			final MessageEndpointFactory messageEndpointFactory,
			final ActivationSpec activationSpec) {
		final Lock writeLock = messageEndpointFactoryMapsLock.writeLock();
		MessageEndpointFactory oldMessageEndpointFactory = null;

		writeLock.lock();
		try {
			oldMessageEndpointFactory = messageEndpointFactories.put(
					messageEndpointFactoryKeyFactory.createMessageEndpointKey(
							activationSpec, messageEndpointFactory),
					messageEndpointFactory);
		} finally {
			writeLock.unlock();
		}

		return oldMessageEndpointFactory;
	}

	public MessageEndpointFactory unregisterMessageEndpointFactory(
			final MessageEndpointFactory messageEndpointFactory,
			final ActivationSpec activationSpec) {
		final Lock writeLock = messageEndpointFactoryMapsLock.writeLock();
		MessageEndpointFactory oldMessageEndpointFactory = null;

		writeLock.lock();
		try {
			oldMessageEndpointFactory = messageEndpointFactories
					.remove(messageEndpointFactoryKeyFactory
							.createMessageEndpointKey(activationSpec,
									messageEndpointFactory));
		} finally {
			writeLock.unlock();
		}

		return oldMessageEndpointFactory;
	}

	public Collection<MessageEndpointFactoryKey> getRegistrationKeys(
			final Class<?> activationSpecClass) {
		final Set<MessageEndpointFactoryKey> messEndpointFactoryKeys = createMessageEndpointFactoryKeys();

		for (final MessageEndpointFactoryKey messageEndpointFactoryKey : messageEndpointFactories
				.keySet()) {
			if (activationSpecClass.isAssignableFrom(messageEndpointFactoryKey
					.getActivationSpec().getClass())) {
				messEndpointFactoryKeys.add(messageEndpointFactoryKey);
			}
		}

		return messEndpointFactoryKeys;
	}

	Set<MessageEndpointFactoryKey> createMessageEndpointFactoryKeys() {
		return new HashSet<MessageEndpointFactoryKey>();
	}

	public MessageEndpointFactory getRegisteredEndpointFactory(
			final MessageEndpointFactoryKey messageEndpointFactoryKey) {
		return messageEndpointFactories.get(messageEndpointFactoryKey);
	}
}
