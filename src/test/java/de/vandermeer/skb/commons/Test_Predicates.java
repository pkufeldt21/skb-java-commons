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

package de.vandermeer.skb.commons;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import org.apache.commons.lang3.text.StrBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for Predicates.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.4 build 150619 (19-Jun-15) for Java 1.8
 */
@RunWith(JUnit4.class)
public class Test_Predicates {

	@Test
	public void testContainsStringsStartingWith(){
		Set<String> set=new HashSet<String>();
		set.add("/");
		set.add("/a");
		set.add("/a/b");
		set.add("/a/b/c");
		set.add("/a/b/c/d");
		set.add("/a/b/c/e");
		set.add("/a/b/y");
		set.add("/a/b/y/w");
		set.add("/a/b/y/z");

		Predicate<StrBuilder> containsString=Predicates.CONTAINS_STRINGS_STARTING_WITH(set);

		assertTrue(containsString.test(new StrBuilder("/")));
		assertTrue(containsString.test(new StrBuilder("/a")));
		assertTrue(containsString.test(new StrBuilder("/a/b")));

		assertTrue(containsString.test(new StrBuilder("/a/b/c")));
		assertFalse(containsString.test(new StrBuilder("/a/b/c/d")));
		assertFalse(containsString.test(new StrBuilder("/a/b/c/e")));

		assertTrue(containsString.test(new StrBuilder("/a/b/y")));
		assertFalse(containsString.test(new StrBuilder("/a/b/y/w")));
		assertFalse(containsString.test(new StrBuilder("/a/b/y/z")));
	}
}
