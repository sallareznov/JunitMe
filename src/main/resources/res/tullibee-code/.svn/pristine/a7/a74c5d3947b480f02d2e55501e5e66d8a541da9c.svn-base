// Copyright 2010-2012 Christopher Redekop
//  
// This file is part of the Tullibee API, a modified version of Interactive
// Brokers' Java API (the IB API).
//  
// The Tullibee API is free software: you can redistribute it and/or modify it
// under the terms of the GNU Lesser General Public License as published by
// the Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//  
// The Tullibee API is distributed in the hope that it will be useful, but
// WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
// or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
// License for more details.
//  
// You should have received a copy of the GNU Lesser General Public License
// along with the Tullibee API.  If not, see <http://www.gnu.org/licenses/>.

package com.ib.client;

import static com.ib.client.EClientErrors.NO_VALID_ID;
import static com.ib.client.EClientErrors.UNKNOWN_ID;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import org.easymock.EasyMock;
import org.junit.Test;

import com.ib.client.AnyWrapper;
import com.ib.client.EException;
import com.ib.client.EReader;
import com.ib.client.EWrapper;

public class EReaderTest {

	@Test
	public void testRunStopped() {
		EWrapper mockEWrapper = EasyMock.createStrictMock(EWrapper.class);
		mockEWrapper.stopRequested();
		
		EReader eReader = new EReader(null, mockEWrapper, 0);

		replay(mockEWrapper);

		eReader.stop();
		eReader.run();

		verify(mockEWrapper);
	}

	@Test
	public void testRunDisconnected() {
		EWrapper mockEWrapper = EasyMock.createStrictMock(EWrapper.class);

		EReader eReader = new EReader(new DataInputStream(
				new ByteArrayInputStream("-1\u0000".getBytes())), mockEWrapper,
				0);

		mockEWrapper.connectionClosed();

		replay(mockEWrapper);

		eReader.run();

		verify(mockEWrapper);
	}

	@Test
	public void testRunException() {
		EWrapper mockEWrapper = EasyMock.createStrictMock(EWrapper.class);

		EReader eReader = new EReader(new DataInputStream(
				new ByteArrayInputStream("5555555\u0000".getBytes())),
				mockEWrapper, 0);

		mockEWrapper.error(isA(EException.class));
		expectLastCall().andDelegateTo(createAnyWrapper());

		replay(mockEWrapper);

		eReader.run();

		verify(mockEWrapper);
	}

	private AnyWrapper createAnyWrapper() {
		return new AnyWrapper() {

			public void error(int id, int errorCode, String errorMsg) {
			}

			public void error(String str) {
			}

			public void error(Exception e) {
				EException eException = (EException) e;

				assertEquals(NO_VALID_ID, eException.getId());
				assertEquals(UNKNOWN_ID.code(), eException.getErrorCode());
			}

			public void connectionClosed() {
			}
		};
	}
}
