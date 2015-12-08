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

import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;

public class ConnectionImplTest {
	private ConnectionImpl cut;
	private EasyMockSupport easyMockSupport;
	private ManagedConnectionImpl mockManagedConnectionImpl;

	@Before
	public void before() {
		easyMockSupport = new EasyMockSupport();
		mockManagedConnectionImpl = easyMockSupport
				.createNiceMock(ManagedConnectionImpl.class);

		cut = new ConnectionImpl(mockManagedConnectionImpl);
	}

	@Test
	public void checkValidity_ShouldNotThrowAnException_WhenConnectionImplIsNew()
			throws Exception {
		easyMockSupport.replayAll();

		cut.checkValidity();

		easyMockSupport.verifyAll();
	}
}
