/*
 * Hibernate, Relational Persistence for Idiomatic Java Copyright (c) 2008, Red
 * Hat Middleware LLC or third-party contributors as indicated by the @author
 * tags or express copyright attribution statements applied by the authors. All
 * third-party contributions are distributed under license by Red Hat Middleware
 * LLC. This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions of the
 * GNU Lesser General Public License, as published by the Free Software
 * Foundation. This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details. You should have received a copy of
 * the GNU Lesser General Public License along with this distribution; if not,
 * write to: Free Software Foundation, Inc. 51 Franklin Street, Fifth Floor
 * Boston, MA 02110-1301 USA
 */
package com.kmetop.demsy.orm.dialect;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.kmetop.demsy.lang.DemsyException;
import com.kmetop.demsy.lang.Str;

/**
 * This class maps a type to names. Associations may be marked with a capacity.
 * Calling the get() method with a type and actual size n will return the
 * associated name with smallest capacity >= n, if available and an unmarked
 * default type otherwise. Eg, setting
 * 
 * <pre>
 * names.put(type, &quot;TEXT&quot;);
 * names.put(type, 255, &quot;VARCHAR($l)&quot;);
 * names.put(type, 65534, &quot;LONGVARCHAR($l)&quot;);
 * </pre>
 * 
 * will give you back the following:
 * 
 * <pre>
 *  names.get(type)         // --&gt; &quot;TEXT&quot; (default)
 *  names.get(type,    100) // --&gt; &quot;VARCHAR(100)&quot; (100 is in [0:255])
 *  names.get(type,   1000) // --&gt; &quot;LONGVARCHAR(1000)&quot; (1000 is in [256:65534])
 *  names.get(type, 100000) // --&gt; &quot;TEXT&quot; (default)
 * </pre>
 * 
 * On the other hand, simply putting
 * 
 * <pre>
 * names.put(type, &quot;VARCHAR($l)&quot;);
 * </pre>
 * 
 * would result in
 * 
 * <pre>
 *  names.get(type)        // --&gt; &quot;VARCHAR($l)&quot; (will cause trouble)
 *  names.get(type, 100)   // --&gt; &quot;VARCHAR(100)&quot;
 *  names.get(type, 10000) // --&gt; &quot;VARCHAR(10000)&quot;
 * </pre>
 * 
 * @author Christoph Beck
 */
public class TypeNames {

	private HashMap weighted = new HashMap();
	private HashMap defaults = new HashMap();

	/**
	 * get default type name for specified type
	 * 
	 * @param typecode
	 *            the type key
	 * @return the default type name associated with specified key
	 */
	public String get(int typecode) throws DemsyException {
		String result = (String) defaults.get(new Integer(typecode));
		if (result == null)
			throw new DemsyException("No Dialect mapping for JDBC type: " + typecode);
		return result;
	}

	/**
	 * get type name for specified type and size
	 * 
	 * @param typecode
	 *            the type key
	 * @param size
	 *            the SQL length
	 * @param scale
	 *            the SQL scale
	 * @param precision
	 *            the SQL precision
	 * @return the associated name with smallest capacity >= size, if available
	 *         and the default type name otherwise
	 */
	public String get(int typecode, int size, int precision, int scale) throws DemsyException {
		Map map = (Map) weighted.get(new Integer(typecode));
		if (map != null && map.size() > 0) {
			// iterate entries ordered by capacity to find first fit
			Iterator entries = map.entrySet().iterator();
			while (entries.hasNext()) {
				Map.Entry entry = (Map.Entry) entries.next();
				if (size <= ((Integer) entry.getKey()).intValue()) {
					return replace((String) entry.getValue(), size, precision, scale);
				}
			}
		}
		return replace(get(typecode), size, precision, scale);
	}

	private static String replace(String type, int size, int precision, int scale) {
		type = Str.replaceOnce(type, "$s", Integer.toString(scale));
		type = Str.replaceOnce(type, "$l", Integer.toString(size));
		return Str.replaceOnce(type, "$p", Integer.toString(precision));
	}

	/**
	 * set a type name for specified type key and capacity
	 * 
	 * @param typecode
	 *            the type key
	 */
	public void put(int typecode, int capacity, String value) {
		TreeMap map = (TreeMap) weighted.get(new Integer(typecode));
		if (map == null) {// add new ordered map
			map = new TreeMap();
			weighted.put(new Integer(typecode), map);
		}
		map.put(new Integer(capacity), value);
	}

	/**
	 * set a default type name for specified type key
	 * 
	 * @param typecode
	 *            the type key
	 */
	public void put(int typecode, String value) {
		defaults.put(new Integer(typecode), value);
	}
}
