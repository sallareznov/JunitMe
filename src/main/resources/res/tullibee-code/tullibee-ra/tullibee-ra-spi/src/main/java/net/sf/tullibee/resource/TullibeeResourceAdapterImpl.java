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

import static java.util.Collections.synchronizedMap;
import static javax.resource.spi.TransactionSupport.TransactionSupportLevel.NoTransaction;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.locks.Lock;

import javax.resource.ResourceException;
import javax.resource.spi.ActivationSpec;
import javax.resource.spi.BootstrapContext;
import javax.resource.spi.ConfigProperty;
import javax.resource.spi.Connector;
import javax.resource.spi.ResourceAdapterInternalException;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.resource.spi.work.Work;
import javax.resource.spi.work.WorkException;
import javax.resource.spi.work.WorkManager;
import javax.transaction.xa.XAResource;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Connector(description = "The Tullibee Resource Adapter provides connectivity to Interactive Brokers through the Tullibee API, in accordance with the Java EE Connector Architecture.", displayName = "Tullibee Resource Adapter", smallIcon = {
		"tullibee-24.gif", "tullibee-32.gif" }, largeIcon = {
		"tullibee-48.gif", "tullibee-64.gif" }, vendorName = "Tullibee", eisType = "Interactive Brokers", licenseRequired = true, licenseDescription = "GNU Lesser General Public License <http://www.gnu.org/licenses/lgpl-3.0.txt>", transactionSupport = NoTransaction)
public class TullibeeResourceAdapterImpl implements TullibeeResourceAdapter {
	private WorkManager workManager;
	private final Map<Work, Object> works;
	private final PropertyChangeSupport propertyChangeSupport;
	private final MessageEndpointFactoryRegistry messageEndpointFactoryRegistry;

	@ConfigProperty(type = String.class, defaultValue = "localhost")
	@NotNull
	private String serverName;

	@ConfigProperty(type = Integer.class, defaultValue = "4001")
	@NotNull
	@Min(1)
	@Max(65536)
	private Integer portNumber;

	public TullibeeResourceAdapterImpl() {
		this(new MessageEndpointFactoryRegistryImpl(),
				synchronizedMap(new WeakHashMap<Work, Object>()));
	}

	public TullibeeResourceAdapterImpl(
			final MessageEndpointFactoryRegistry messageEndpointFactoryRegistry,
			final Map<Work, Object> works) {
		this.works = works;
		this.messageEndpointFactoryRegistry = messageEndpointFactoryRegistry;
		propertyChangeSupport = createPropertyChangeSupport(this);
	}

	PropertyChangeSupport createPropertyChangeSupport(final Object object) {
		return new PropertyChangeSupport(object);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((portNumber == null) ? 0 : portNumber.hashCode());
		result = prime * result
				+ ((serverName == null) ? 0 : serverName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		TullibeeResourceAdapterImpl other = (TullibeeResourceAdapterImpl) obj;
		if (portNumber == null) {
			if (other.portNumber != null) {
				return false;
			}
		} else if (!portNumber.equals(other.portNumber)) {
			return false;
		}
		if (serverName == null) {
			if (other.serverName != null) {
				return false;
			}
		} else if (!serverName.equals(other.serverName)) {
			return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public void endpointActivation(
			final MessageEndpointFactory messageEndpointFactory,
			final ActivationSpec activationSpec) throws ResourceException {
		final MessageEndpointFactoryRegistry messageEndpointFactoryRegistry = getMessageEndpointFactoryRegistry();
		final Lock messageEndpoingFactoryRegistryWriteLock = messageEndpointFactoryRegistry
				.writeLock();
		try {
			messageEndpoingFactoryRegistryWriteLock.lock();

			messageEndpointFactoryRegistry.registerMessageEndpointFactory(
					messageEndpointFactory, activationSpec);
		} finally {
			messageEndpoingFactoryRegistryWriteLock.unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void endpointDeactivation(
			final MessageEndpointFactory messageEndpointFactory,
			final ActivationSpec activationSpec) {
		final MessageEndpointFactoryRegistry messageEndpointFactoryRegistry = getMessageEndpointFactoryRegistry();
		final Lock messageEndpoingFactoryRegistryWriteLock = messageEndpointFactoryRegistry
				.writeLock();

		try {
			messageEndpoingFactoryRegistryWriteLock.lock();

			messageEndpointFactoryRegistry.unregisterMessageEndpointFactory(
					messageEndpointFactory, activationSpec);
		} finally {
			messageEndpoingFactoryRegistryWriteLock.unlock();
		}
	}

	public XAResource[] getXAResources(final ActivationSpec[] specs)
			throws ResourceException {
		return null;
	}

	public void start(final BootstrapContext ctx)
			throws ResourceAdapterInternalException {
		setWorkManager(ctx.getWorkManager());
	}

	public void stop() {
		for (final Work work : works.keySet()) {
			work.release();
		}
	}

	/**
	 * Provides read access to the {@code serverName} property.
	 * 
	 * @return serverName
	 */
	public String getServerName() {
		return serverName;
	}

	/**
	 * Provides write access to the {@code serverName} property.
	 * 
	 * @param serverName
	 *            the new {@code serverName} value
	 */
	public void setServerName(final String serverName) {
		getPropertyChangeSupport().firePropertyChange("ServerName",
				this.serverName, this.serverName = serverName);
	}

	/**
	 * Provides read access to the {@code portNumber} property.
	 * 
	 * @return portNumber
	 */
	public Integer getPortNumber() {
		return portNumber;
	}

	/**
	 * Provides write access to the {@code portNumber} property.
	 * 
	 * @param portNumber
	 *            the new {@code portNumber} value
	 */
	public void setPortNumber(final Integer portNumber) {
		getPropertyChangeSupport().firePropertyChange("PortNumber",
				this.portNumber, this.portNumber = portNumber);
	}

	/**
	 * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public void addPropertyChangeListener(final PropertyChangeListener listener) {
		getPropertyChangeSupport().addPropertyChangeListener(listener);
	}

	/**
	 * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public void removePropertyChangeListener(
			final PropertyChangeListener listener) {
		getPropertyChangeSupport().removePropertyChangeListener(listener);
	}

	public void scheduleWork(final Work work) throws WorkException {
		getWorks().put(work, null);
		getWorkManager().scheduleWork(work);
	}

	public long startWork(final Work work) throws WorkException {
		getWorks().put(work, null);
		return getWorkManager().startWork(work);
	}

	/**
	 * Provides read access to the {@code messageEndpointFactoryRegistry}
	 * property.
	 * 
	 * @return messageEndpointFactoryRegistry
	 */
	public MessageEndpointFactoryRegistry getMessageEndpointFactoryRegistry() {
		return messageEndpointFactoryRegistry;
	}

	/**
	 * Provides read access to the {@code workManager} property.
	 * 
	 * @return workManager
	 */
	WorkManager getWorkManager() {
		return workManager;
	}

	/**
	 * Provides write access to the {@code workManager} property.
	 * 
	 * @param workManager
	 *            the new {@code workManager} value
	 */
	void setWorkManager(WorkManager workManager) {
		this.workManager = workManager;
	}

	/**
	 * Provides read access to the {@code works} property.
	 * 
	 * @return works
	 */
	Map<Work, Object> getWorks() {
		return works;
	}

	/**
	 * Provides read access to the {@code propertyChangeSupport} property.
	 * 
	 * @return propertyChangeSupport
	 */
	PropertyChangeSupport getPropertyChangeSupport() {
		return propertyChangeSupport;
	}
}
