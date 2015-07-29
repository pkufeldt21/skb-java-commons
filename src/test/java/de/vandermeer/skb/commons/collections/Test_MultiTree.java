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
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.vandermeer.skb.base.composite.Com_Coin;
import de.vandermeer.skb.base.composite.Com_Leaf;
import de.vandermeer.skb.base.composite.Com_Node;
import de.vandermeer.skb.base.composite.Com_Top;
import de.vandermeer.skb.commons.collections.FlatMultiTree;
import de.vandermeer.skb.commons.collections.Tree;

/**
 * Tests for multi tree.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.4 build 150701 (01-Jul-15) for Java 1.8
 */
public class Test_MultiTree {

	@Test public void testTypes(){
		Com_Top t;
		t=new FlatMultiTree<Object>();
		assertTrue(t instanceof Tree);
		assertTrue(t instanceof Com_Node);
		assertFalse(t instanceof Com_Leaf);
		assertFalse(t instanceof Com_Coin);
	}

	@Test public void testSettings(){
		FlatMultiTree<Object> table=new FlatMultiTree<>();
		assertTrue(table.autoRoot);
	}

	@Test public void testConstructor(){
		FlatMultiTree<String> tree=new FlatMultiTree<String>();

		assertEquals(0, tree.sval.size());			// tree is empty
		assertEquals(null, tree.getValue("/"));		// nothing in the tree

		//testing keys and empty. keys is not empty (root node) but tree should say it is empty
		assertTrue(tree.keys().isEmpty());		//sval is empty
		assertTrue(tree.isEmpty());				//tree is empty
	}
}
