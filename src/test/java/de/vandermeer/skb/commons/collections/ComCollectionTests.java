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

package de.vandermeer.skb.commons.collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.vandermeer.skb.collections.ListStrategy;
import de.vandermeer.skb.collections.SetStrategy;
import de.vandermeer.skb.commons.collections.ComCollection;
import de.vandermeer.skb.composite.CompositeObject;
import de.vandermeer.skb.composite.SimpleObject;
import de.vandermeer.skb.composite.SkbObject;
import de.vandermeer.skb.composite.SpecialObject;

/**
 * Tests for the ComCollection.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.2 build 140626 (26-Jun-14) with Java 1.8
 */
public class ComCollectionTests {

	@Test public void testTypes(){
		SkbObject t;
		t=new ComCollection<Object>();
		assertTrue(t instanceof ComCollection);
		assertTrue(t instanceof CompositeObject);
		assertFalse(t instanceof SimpleObject);
		assertFalse(t instanceof SpecialObject);
	}

	@Test public void testConstructorDefault(){
		ComCollection<Object> cc=new ComCollection<Object>();

		assertEquals(ListStrategy.ARRAY_LIST, cc.strategy);
		assertEquals(0, cc.sval.size());
	}

	@Test public void testConstructorStrategy(){
		ComCollection<Object> cc=new ComCollection<Object>(SetStrategy.HASH_SET);

		assertEquals(SetStrategy.HASH_SET, cc.strategy);
		assertNotSame(ListStrategy.ARRAY_LIST, cc.strategy);
		assertEquals(0, cc.sval.size());
	}

	@Test public void testConstructorStrategyColl(){
		ComCollection<Object> load=new ComCollection<Object>(SetStrategy.HASH_SET);
		load.add("String 1");
		load.add(new Integer(20));
		load.add(true);

		ComCollection<Object> cc=new ComCollection<Object>(ListStrategy.LINKED_LIST, load);
		assertEquals(ListStrategy.LINKED_LIST, cc.strategy);
		assertNotSame(ListStrategy.ARRAY_LIST, cc.strategy);
		assertEquals(3, cc.sval.size());

		for(Object obj:cc.sval){
			assertTrue(load.contains(obj));
		}
	}

	@Test public void testGetFirst(){
		//for a list
		ComCollection<String> cc=new ComCollection<String>(ListStrategy.ARRAY_LIST);
		assertNull(cc.getFirst());
		cc.add("st1");
		assertEquals("st1", cc.getFirst());
		cc.add("st2");
		assertEquals("st1", cc.getFirst());
		cc.remove("st1");
		assertEquals("st2", cc.getFirst());

		//and now for a set
		cc=new ComCollection<String>(SetStrategy.LINKED_HASH_SET);
		assertNull(cc.getFirst());
		cc.add("st1");
		assertEquals("st1", cc.getFirst());
		cc.add("st2");
		assertEquals("st1", cc.getFirst());
		cc.remove("st1");
		assertEquals("st2", cc.getFirst());
	}
}
