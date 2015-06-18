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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.vandermeer.skb.commons.collections.FlatTable;
import de.vandermeer.skb.commons.collections.PropertyTable;
import de.vandermeer.skb.composite.CompositeObject;
import de.vandermeer.skb.composite.SimpleObject;
import de.vandermeer.skb.composite.SkbObject;
import de.vandermeer.skb.composite.SpecialObject;

/**
 * Tests for Property Table.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.4-SNAPSHOT build 150618 (18-Jun-15) for Java 1.8
 */
public class PropertyTableTests {

	@Test public void testTypes(){
		SkbObject t;
		t=new PropertyTable(null);
		assertTrue(t instanceof PropertyTable);
		assertTrue(t instanceof FlatTable);
		assertTrue(t instanceof CompositeObject);
		assertFalse(t instanceof SimpleObject);
		assertFalse(t instanceof SpecialObject);
	}

	@Test public void testC(){
		
	}
}
