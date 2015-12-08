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

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.beans.PropertyChangeSupport;
import java.util.Map;
import java.util.concurrent.locks.Lock;

import javax.resource.spi.ActivationSpec;
import javax.resource.spi.BootstrapContext;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.resource.spi.work.Work;
import javax.resource.spi.work.WorkManager;

import org.easymock.EasyMockSupport;
import org.easymock.IAnswer;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("unchecked")
public class TullibeeResourceAdapterImplTest {
	private static final long LONG = 1L;
	private EasyMockSupport easyMockSupport;
	private TullibeeResourceAdapterImpl cut;
	private MessageEndpointFactoryRegistry mockMessageEndpointFactoryRegistry;
	private Map<Work, Object> mockWorks;
	private PropertyChangeSupport mockPropertyChangeSupport;

	@Before
	public void before() {
		easyMockSupport = new EasyMockSupport();
		mockMessageEndpointFactoryRegistry = easyMockSupport
				.createMock(MessageEndpointFactoryRegistry.class);
		mockWorks = easyMockSupport.createMock(Map.class);
		mockPropertyChangeSupport = easyMockSupport
				.createMock(PropertyChangeSupport.class);

		cut = new TullibeeResourceAdapterImpl(
				mockMessageEndpointFactoryRegistry, mockWorks) {
			@Override
			PropertyChangeSupport createPropertyChangeSupport(Object object) {
				return mockPropertyChangeSupport;
			}
		};
	}

	@Test
	public void start_ShouldSetWorkManager_Always() throws Exception {
		WorkManager mockWorkManager = easyMockSupport
				.createNiceMock(WorkManager.class);
		BootstrapContext mockBootstrapContext = easyMockSupport
				.createMock(BootstrapContext.class);

		expect(mockBootstrapContext.getWorkManager())
				.andReturn(mockWorkManager);

		easyMockSupport.replayAll();

		cut.start(mockBootstrapContext);

		assertEquals(mockWorkManager, cut.getWorkManager());

		easyMockSupport.verifyAll();
	}

	@Test
	public void endpointActivation_ShouldRegisterEndpoint_Always() throws Exception {
		MessageEndpointFactory mockMessageEndpointFactory = easyMockSupport
				.createMock(MessageEndpointFactory.class);
		ActivationSpec mockActivationSpec = easyMockSupport
				.createNiceMock(ActivationSpec.class);
		Lock mockLock = easyMockSupport.createMock(Lock.class);

		mockLock.lock();
		mockLock.unlock();

		expect(mockMessageEndpointFactoryRegistry.writeLock()).andReturn(
				mockLock);
		expect(
				mockMessageEndpointFactoryRegistry
						.registerMessageEndpointFactory(
								mockMessageEndpointFactory, mockActivationSpec))
				.andReturn(mockMessageEndpointFactory);

		easyMockSupport.replayAll();

		cut.endpointActivation(mockMessageEndpointFactory, mockActivationSpec);

		easyMockSupport.verifyAll();
	}

	@Test
	public void endpointActivation_ShouldUnlockRegistry_Always()
			throws Exception {
		MessageEndpointFactory mockMessageEndpointFactory = easyMockSupport
				.createMock(MessageEndpointFactory.class);
		ActivationSpec mockActivationSpec = easyMockSupport
				.createNiceMock(ActivationSpec.class);
		Lock mockLock = easyMockSupport.createMock(Lock.class);

		mockLock.lock();
		mockLock.unlock();

		expect(mockMessageEndpointFactoryRegistry.writeLock()).andReturn(
				mockLock);
		expect(
				mockMessageEndpointFactoryRegistry
						.registerMessageEndpointFactory(
								mockMessageEndpointFactory, mockActivationSpec))
				.andAnswer(new IAnswer<MessageEndpointFactory>() {
					@Override
					public MessageEndpointFactory answer() throws Throwable {
						throw new Exception();
					}
				});

		easyMockSupport.replayAll();

		try {
			cut.endpointActivation(mockMessageEndpointFactory,
					mockActivationSpec);
			fail();
		} catch (Exception e) {
		}

		easyMockSupport.verifyAll();
	}

	@Test
	public void endpointDeactivation_ShouldUnegisterEndpoint_Always() throws Exception {
		MessageEndpointFactory mockMessageEndpointFactory = easyMockSupport
				.createMock(MessageEndpointFactory.class);
		ActivationSpec mockActivationSpec = easyMockSupport
				.createNiceMock(ActivationSpec.class);
		Lock mockLock = easyMockSupport.createMock(Lock.class);

		mockLock.lock();
		mockLock.unlock();

		expect(mockMessageEndpointFactoryRegistry.writeLock()).andReturn(
				mockLock);
		expect(
				mockMessageEndpointFactoryRegistry
						.unregisterMessageEndpointFactory(
								mockMessageEndpointFactory, mockActivationSpec))
				.andReturn(mockMessageEndpointFactory);

		easyMockSupport.replayAll();

		cut
				.endpointDeactivation(mockMessageEndpointFactory,
						mockActivationSpec);

		easyMockSupport.verifyAll();
	}

	@Test
	public void endpointDeactivation_ShouldUnlockRegistry_Always()
			throws Exception {
		MessageEndpointFactory mockMessageEndpointFactory = easyMockSupport
				.createMock(MessageEndpointFactory.class);
		ActivationSpec mockActivationSpec = easyMockSupport
				.createNiceMock(ActivationSpec.class);
		Lock mockLock = easyMockSupport.createMock(Lock.class);

		mockLock.lock();
		mockLock.unlock();

		expect(mockMessageEndpointFactoryRegistry.writeLock()).andReturn(
				mockLock);
		expect(
				mockMessageEndpointFactoryRegistry
						.unregisterMessageEndpointFactory(
								mockMessageEndpointFactory, mockActivationSpec))
				.andAnswer(new IAnswer<MessageEndpointFactory>() {
					@Override
					public MessageEndpointFactory answer() throws Throwable {
						throw new Exception();
					}
				});

		easyMockSupport.replayAll();

		try {
			cut.endpointDeactivation(mockMessageEndpointFactory,
					mockActivationSpec);
			fail();
		} catch (Exception e) {
		}

		easyMockSupport.verifyAll();
	}

	@Test
	public void startWork_ShouldRegisterAndDelegateWork_Always()
			throws Exception {
		WorkManager mockWorkManager = easyMockSupport
				.createMock(WorkManager.class);
		Work mockWork = easyMockSupport.createNiceMock(Work.class);

		expect(mockWorks.put(mockWork, null)).andReturn(null);
		expect(mockWorkManager.startWork(mockWork)).andReturn(LONG);

		easyMockSupport.replayAll();

		cut.setWorkManager(mockWorkManager);
		assertEquals(LONG, cut.startWork(mockWork));

		easyMockSupport.verifyAll();
	}
}
