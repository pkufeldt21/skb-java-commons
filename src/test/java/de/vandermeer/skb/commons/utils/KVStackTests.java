/* Copyright 2014 Sven van der Meer <vdmeer.sven@mykolab.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.vandermeer.skb.commons.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for the atomic stack.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.3 build 150618 (18-Jun-15) for Java 1.8
 */
public class KVStackTests {

	@Test public void testConstructor(){
		KVStack st=new KVStack();

		assertEquals(0, st.sval.size());
		assertEquals("@", st.separator);
	}

	@Test public void testPush(){
		KVStack st=new KVStack();

		assertTrue (st.push("key1", "value2"));
		assertFalse(st.push("key1", "value2"));

		assertTrue (st.push("key2", "value2"));
		assertFalse(st.push("key2", "value2"));

		assertTrue (st.push("key3", "value3"));
		assertFalse(st.push("key3", "value3"));
	}

	@Test public void testReset(){
		KVStack st=new KVStack();

		st.push("key1", "value1");
		st.push("key2", "value2");
		st.push("key3", "value3");

		st.reset();
		assertEquals(0, st.sval.size());
	}

	@Test public void testPop(){
		KVStack st=new KVStack();

		st.push("key1", "value1");
		st.push("key2", "value2");
		st.push("key3", "value3");

		st.pop();
		assertFalse(st.sval.contains("key3"+st.separator+"value3"));

		st.pop();
		assertFalse(st.sval.contains("key2"+st.separator+"value2"));

		st.pop();
		assertFalse(st.sval.contains("key1"+st.separator+"value1"));
	}
}
