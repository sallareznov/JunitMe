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

import static com.ib.client.EClientErrors.ALREADY_CONNECTED;
import static com.ib.client.EClientErrors.NO_VALID_ID;
import static com.ib.client.EException.NO_VALID_CODE;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class EExceptionTest {

	private static final int CLIENT_ID = 5;
	private EException m_eException;

	@Test
	public void testMessageConstructor() {
		m_eException = new EException("Message");

		testProperties(NO_VALID_ID, NO_VALID_CODE,
				"Message [id = -1, errorCode = -1]");
	}

	private void testProperties(int id, int code, String message) {
		assertEquals(id, m_eException.getId());
		assertEquals(code, m_eException.getErrorCode());
		assertEquals(message, m_eException.getMessage());
	}

	@Test
	public void testFullConstructor() {
		m_eException = new EException(CLIENT_ID, ALREADY_CONNECTED);

		testProperties(CLIENT_ID, ALREADY_CONNECTED.code(),
				"Already connected. [id = 5, errorCode = 501]");
	}

}
