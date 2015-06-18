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
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.vandermeer.skb.base.Skb_Transformer;
import de.vandermeer.skb.commons.collections.ComCollection;
import de.vandermeer.skb.commons.collections.CompositeTransformers;
import de.vandermeer.skb.commons.collections.FlatTree;
import de.vandermeer.skb.commons.collections.Tree;
import de.vandermeer.skb.composite.CompositeObject;
import de.vandermeer.skb.composite.specialobject.NONone;
import de.vandermeer.skb.composite.specialobject.NONull;

/**
 * Tests for Composite Transformers.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.4-SNAPSHOT build 150618 (18-Jun-15) for Java 1.8
 */
public class Test_CompositeTransformers {
	Skb_Transformer<Object, CompositeObject> tr=CompositeTransformers.EXPLODE_OBJECT();

	@Test public void testExplodeDefaults(){
		assertEquals(NONull.get, this.tr.transform(null));
		assertNull(this.tr.transform(""));

		assertEquals(NONone.get, this.tr.transform(" "));
		assertEquals(NONone.get, this.tr.transform("foo bar"));

		assertNotSame(NONone.get, this.tr.transform("%"));
		assertNotSame(NONone.get, this.tr.transform("%,"));
		assertNotSame(NONone.get, this.tr.transform(","));

		assertNotSame(NONone.get, this.tr.transform("foo%bar"));
		assertNotSame(NONone.get, this.tr.transform("foo%bar,"));
		assertNotSame(NONone.get, this.tr.transform("foo,bar"));
	}

	@Test public void testExplodePercent(){
		assertTrue(this.tr.transform("%") instanceof FlatTree);
		assertTrue(this.tr.transform("foo%bar") instanceof FlatTree);

		Tree<?> tree;
		tree=(Tree<?>)this.tr.transform("%");
		assertEquals(0, tree.size());	//size==0 means no root

		tree=(Tree<?>)this.tr.transform("foo%");
		assertEquals(0, tree.size());	//size==0 no root

		tree=(Tree<?>)this.tr.transform("%bar");
		assertEquals(1, tree.size());	//size==1 means only root, value set to bar

		tree=(Tree<?>)this.tr.transform("foo%bar");
		assertEquals(2, tree.size());	//size==2 , should have foo::=bar now
		assertTrue(tree.containsNode("foo"));
		assertEquals("bar", tree.getValue("foo"));


		tree=(Tree<?>)this.tr.transform("foo%bar,");
		assertEquals(2, tree.size());	//size==2 , nothing added beside foo

		tree=(Tree<?>)this.tr.transform("foo%bar,%");
		assertEquals(2, tree.size());	//size==2 , nothing added beside foo

		tree=(Tree<?>)this.tr.transform("foo1%bar1,foo2%bar2");
		assertEquals(3, tree.size());	//size==2 , foo1 and foo2 now
		assertTrue(tree.containsNode("foo1"));
		assertEquals("bar1", tree.getValue("foo1"));
		assertTrue(tree.containsNode("foo2"));
		assertEquals("bar2", tree.getValue("foo2"));
	}

	@Test public void testExplodeComma(){
		assertTrue(this.tr.transform(",") instanceof ComCollection);
		assertTrue(this.tr.transform("foo,bar") instanceof ComCollection);

		ComCollection<?> coll;
		coll=(ComCollection<?>)this.tr.transform(",");
		assertEquals(0, coll.size());	//empty

		coll=(ComCollection<?>)this.tr.transform("one,");
		assertEquals(1, coll.size());	//empty
		assertTrue(coll.contains("one"));

		coll=(ComCollection<?>)this.tr.transform("one, two");
		assertEquals(2, coll.size());	//empty
		assertTrue(coll.contains("one"));
		assertTrue(coll.contains("two"));

		coll=(ComCollection<?>)this.tr.transform("one, two,tree");
		assertEquals(3, coll.size());	//empty
		assertTrue(coll.contains("one"));
		assertTrue(coll.contains("two"));
		assertTrue(coll.contains("tree"));
	}
}
