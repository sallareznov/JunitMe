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

@SuppressWarnings("serial")
public class EException extends Exception {
	public final static int NO_VALID_CODE = -1;
	
	private final int m_id;
	private final int m_errorCode;

	public EException(String message) {
		this(NO_VALID_ID, NO_VALID_CODE, message);
	}

	public EException(int id, EClientErrors.CodeMsgPair codeMsgPair) {
		this(id, codeMsgPair.code(), codeMsgPair.msg());
	}
	
	public EException(int id, int errorCode, String errorMsg) {
		super(errorMsg);

		m_id = id;
		m_errorCode = errorCode;
	}

	/**
	 * Provides read access to the {@code id} property.
	 * 
	 * @return id
	 */
	public int getId() {
		return m_id;
	}

	/**
	 * Provides read access to the {@code errorCode} property.
	 * 
	 * @return errorCode
	 */
	public int getErrorCode() {
		return m_errorCode;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMessage() {
		return String.format("%s [id = %d, errorCode = %d]",
				super.getMessage(), m_id, m_errorCode);
	}
}
