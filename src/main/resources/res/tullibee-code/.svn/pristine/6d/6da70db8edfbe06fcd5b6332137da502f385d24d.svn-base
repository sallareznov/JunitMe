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
import static com.ib.client.EClientErrors.NOT_CONNECTED;
import static com.ib.client.EClientErrors.NO_VALID_ID;
import static com.ib.client.EClientErrors.UPDATE_TWS;
import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

public class EClientSocketTest {

	private static final int SERVER_VERSION = 38;
	private static final int CLIENT_VERSION = 46;
	private static final byte[] EOL = { 0 };
	private static final String TWS_TIME = "11:59 PM";

	private static final byte[] CURRENT_SERVER_RESPONSE = (SERVER_VERSION
			+ "\u0000" + TWS_TIME + "\u0000").getBytes();
	private static final byte[] OUTDATED_SERVER_RESPONSE = ((SERVER_VERSION - 1) + "\u0000")
			.getBytes();

	private static final String HOST = "host";
	private static final int PORT = 80;
	private static final int CLIENT_ID = 9;

	private EClientSocket m_eClientSocket;
	private Socket m_mockSocket;
	private DataInputStream m_dataInputStream;
	private DataOutputStream m_mockDataOutputStream;

	@Before
	public void before() {
		m_mockSocket = createMock(Socket.class);
		m_mockDataOutputStream = createMock(DataOutputStream.class);

		m_eClientSocket = new EClientSocket() {
			@Override
			Socket createSocket(String host, int port) throws IOException {
				return m_mockSocket;
			}

			@Override
			DataInputStream createDataInputStream(Socket socket)
					throws IOException {
				return m_dataInputStream;
			}

			@Override
			DataOutputStream createDataOutputStream(Socket socket)
					throws IOException {
				return m_mockDataOutputStream;
			}
		};
	}

	@Test
	public void testIsConnectedNullSocket() {
		replayMembers();

		assertFalse(m_eClientSocket.isConnected());

		verifyMembers();
	}

	private void replayMembers() {
		replay(m_mockSocket, m_mockDataOutputStream);
	}

	private void verifyMembers() {
		verify(m_mockSocket, m_mockDataOutputStream);
	}

	@Test
	public void testIsConnectedDisconnectedSocket() throws Exception {
		setupMockSocket(false, false);

		setupMockDataOutputStream();

		replayMembers();

		testIsConnected(false);

		verifyMembers();
	}

	@Test
	public void testIsConnectedClosedSocket() throws Exception {
		setupMockSocket(true, true);

		setupMockDataOutputStream();

		replayMembers();

		testIsConnected(false);

		verifyMembers();
	}

	@Test
	public void testIsConnected() throws Exception {
		setupMockSocket(true, false);

		setupMockDataOutputStream();

		replayMembers();

		testIsConnected(true);

		verifyMembers();
	}

	private void setupMockSocket(boolean isConnected, boolean isClosed)
			throws IOException {
		expect(m_mockSocket.isConnected()).andReturn(isConnected).anyTimes();
		expect(m_mockSocket.isClosed()).andReturn(isClosed).anyTimes();
	}

	private void testIsConnected(boolean expectedResult) throws Exception {
		setupDataInputStream(CURRENT_SERVER_RESPONSE);

		m_eClientSocket.eConnect(HOST, PORT, CLIENT_ID);

		assertEquals(expectedResult, m_eClientSocket.isConnected());
	}

	private void setupDataInputStream(byte[] serverResponse) {
		m_dataInputStream = new DataInputStream(new ByteArrayInputStream(
				serverResponse));
	}

	@Test
	public void testEConnectOutdatedServer() throws IOException {
		setupMockSocket(false, false);
		setupDataInputStream(OUTDATED_SERVER_RESPONSE);

		m_mockDataOutputStream.write(aryEq(asBytes(CLIENT_VERSION)));
		m_mockDataOutputStream.write(aryEq(EOL));

		replayMembers();

		try {
			m_eClientSocket.eConnect(HOST, PORT, CLIENT_ID);

			fail();
		} catch (EException e) {
			assertEquals(NO_VALID_ID, e.getId());
			assertEquals(UPDATE_TWS.code(), e.getErrorCode());
		}

		verifyMembers();
	}

	@Test
	public void testEConnectIoException() throws Exception {
		setupMockSocket(false, false);
		setupDataInputStream(OUTDATED_SERVER_RESPONSE);
		final IOException ioException = new IOException();

		m_mockDataOutputStream.write(aryEq(asBytes(CLIENT_VERSION)));
		EasyMock.expectLastCall().andThrow(ioException);

		replayMembers();

		try {
			m_eClientSocket.eConnect(HOST, PORT, CLIENT_ID);

			fail();
		} catch (IOException e) {
			assertEquals(ioException, e);
		}

		verifyMembers();
	}

	@Test
	public void testConnectionCycle() throws Exception {
		setupMockSocket(true, false);
		setupDataInputStream(CURRENT_SERVER_RESPONSE);

		m_mockSocket.close();

		setupMockDataOutputStream();

		replayMembers();

		m_eClientSocket.eConnect(HOST, PORT, CLIENT_ID);

		assertEquals(SERVER_VERSION, m_eClientSocket.serverVersion());
		assertEquals(TWS_TIME, m_eClientSocket.TwsConnectionTime());
		assertEquals(m_dataInputStream, m_eClientSocket.dataInputStream());

		try {
			m_eClientSocket.eConnect(HOST, PORT, CLIENT_ID);

			fail();
		} catch (EException e) {
			assertEquals(NO_VALID_ID, e.getId());
			assertEquals(ALREADY_CONNECTED.code(), e.getErrorCode());
		}

		m_eClientSocket.eDisconnect();

		try {
			m_eClientSocket.serverVersion();
			fail();
		} catch (EException e) {
			assertEquals(NO_VALID_ID, e.getId());
			assertEquals(NOT_CONNECTED.code(), e.getErrorCode());
		}

		try {
			m_eClientSocket.TwsConnectionTime();
			fail();
		} catch (EException e) {
			assertEquals(NO_VALID_ID, e.getId());
			assertEquals(NOT_CONNECTED.code(), e.getErrorCode());
		}

		try {
			m_eClientSocket.dataInputStream();
			fail();
		} catch (EException e) {
			assertEquals(NO_VALID_ID, e.getId());
			assertEquals(NOT_CONNECTED.code(), e.getErrorCode());
		}

		verifyMembers();
	}

	private void setupMockDataOutputStream() throws IOException {
		m_mockDataOutputStream.write(aryEq(asBytes(CLIENT_VERSION)));
		m_mockDataOutputStream.write(aryEq(EOL));
		m_mockDataOutputStream.write(aryEq(asBytes(CLIENT_ID)));
		m_mockDataOutputStream.write(aryEq(EOL));
	}

	@Test
	public void testEDisconnectNotConnected() throws IOException, EException {
		m_eClientSocket.eDisconnect();

		replayMembers();


		try {
			m_eClientSocket.serverVersion();
			fail();
		} catch (EException e) {
			assertEquals(NO_VALID_ID, e.getId());
			assertEquals(NOT_CONNECTED.code(), e.getErrorCode());
		}

		try {
			m_eClientSocket.TwsConnectionTime();
			fail();
		} catch (EException e) {
			assertEquals(NO_VALID_ID, e.getId());
			assertEquals(NOT_CONNECTED.code(), e.getErrorCode());
		}

		try {
			m_eClientSocket.dataInputStream();
			fail();
		} catch (EException e) {
			assertEquals(NO_VALID_ID, e.getId());
			assertEquals(NOT_CONNECTED.code(), e.getErrorCode());
		}

		verifyMembers();
	}

	private byte[] asBytes(int integer) {
		return String.valueOf(integer).getBytes();
	}
	
	@Test
	public void FileTests() {
		System.out.println(new File(new File("C:\\temp", ""), "foo.txt"));
	}
}
