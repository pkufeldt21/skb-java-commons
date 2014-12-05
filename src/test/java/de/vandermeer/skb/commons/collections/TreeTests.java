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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.text.StrBuilder;
import org.junit.Test;

import de.vandermeer.skb.commons.collections.Tree;

/**
 * Tests for tree (and table) utilities.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.2 build 140626 (26-Jun-14) with Java 1.8
 */
public class TreeTests {

	@Test public void testRemoveNode(){
		String separator="/";

		Map<String, String> map=new HashMap<String, String>();

		map.put("/", null);
		map.put("/a", "level1");
		map.put("/a/b", "level2");
		map.put("/a/b/c", "level3");
		map.put("/a/b/c/d", "level4");
		map.put("/a/b/c/e", "level4");
		map.put("/a/b/y", "level3");
		map.put("/a/b/y/w", "level4");
		map.put("/a/b/y/z", "level4");

		assertEquals(9, map.size());

		assertFalse(Tree.removeNode(null, map, separator));

		assertTrue(Tree.removeNode(new StrBuilder("/a/b/y/z"), map, separator));
		assertFalse(map.containsKey("/a/b/y/z"));

		assertTrue(Tree.removeNode(new StrBuilder("/a/b/y"), map, separator));
		assertFalse(map.containsKey("/a/b/y"));
		assertFalse(map.containsKey("/a/b/y/w"));

		assertTrue(Tree.removeNode(new StrBuilder("/a/b"), map, separator));
		assertFalse(map.containsKey("/a/b"));
		assertFalse(map.containsKey("/a/b/c"));
		assertFalse(map.containsKey("/a/b/c/d"));
		assertFalse(map.containsKey("/a/b/c/e"));

		assertEquals(2, map.size());

		assertTrue(Tree.removeNode(new StrBuilder("/"), map, separator));
		assertEquals(0, map.size());
	}
}
