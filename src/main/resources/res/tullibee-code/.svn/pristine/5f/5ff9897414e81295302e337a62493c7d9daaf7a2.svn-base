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

public class TagValue {
	
	public String m_tag;
	public String m_value;
	
	public TagValue() {
	}
	
	public TagValue(String p_tag, String p_value) {
		m_tag = p_tag;
		m_value = p_value;
	}
	
	public boolean equals(Object p_other) {
		
		if( this == p_other)
            return true;

        if( p_other == null)
            return false;
        
        TagValue l_theOther = (TagValue)p_other;

        if( Util.StringCompare(m_tag, l_theOther.m_tag) != 0 ||
        	Util.StringCompare(m_value, l_theOther.m_value) != 0) {
        	return false;
        }

		return true;
	}
}
