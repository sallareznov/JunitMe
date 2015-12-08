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
 * UnderComp.java
 *
 */

package com.ib.client;

public class UnderComp {
	
	public int    m_conId;
	public double m_delta;
	public double m_price;
	
	public UnderComp() {
		m_conId = 0;
		m_delta = 0;
		m_price = 0;
	}
	
    public boolean equals(Object p_other) {

    	if (this == p_other) {
    		return true;
    	}

    	if (p_other == null || !(p_other instanceof UnderComp)) {
    		return false;
    	}

        UnderComp l_theOther = (UnderComp)p_other;
        
        if (m_conId != l_theOther.m_conId) {
        	return false;
        }
        if (m_delta != l_theOther.m_delta) {
        	return false;
        }
        if (m_price != l_theOther.m_price) {
        	return false;
        }
        
        return true;
    }
}
