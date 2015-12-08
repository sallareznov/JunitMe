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

/*
 * ExecutionFilter.java
 *
 */
package com.ib.client;

public class ExecutionFilter{
    public int 		m_clientId;
    public String 	m_acctCode;
    public String 	m_time;
    public String 	m_symbol;
    public String 	m_secType;
    public String 	m_exchange;
    public String 	m_side;

    public ExecutionFilter() {
        m_clientId = 0;
    }

    public ExecutionFilter( int p_clientId, String p_acctCode, String p_time,
    		String p_symbol, String p_secType, String p_exchange, String p_side) {
        m_clientId = p_clientId;
        m_acctCode = p_acctCode;
        m_time = p_time;
        m_symbol = p_symbol;
        m_secType = p_secType;
        m_exchange = p_exchange;
        m_side = p_side;
    }

    public boolean equals(Object p_other) {
        boolean l_bRetVal = false;

        if ( p_other == null ) {
            l_bRetVal = false;
		}
        else if ( this == p_other ) {
            l_bRetVal = true;
        }
        else {
            ExecutionFilter l_theOther = (ExecutionFilter)p_other;
            l_bRetVal = (m_clientId == l_theOther.m_clientId &&
                    m_acctCode.equalsIgnoreCase( l_theOther.m_acctCode) &&
                    m_time.equalsIgnoreCase( l_theOther.m_time) &&
                    m_symbol.equalsIgnoreCase( l_theOther.m_symbol) &&
                    m_secType.equalsIgnoreCase( l_theOther.m_secType) &&
                    m_exchange.equalsIgnoreCase( l_theOther.m_exchange) &&
                    m_side.equalsIgnoreCase( l_theOther.m_side) );
        }
        return l_bRetVal;
    }
}