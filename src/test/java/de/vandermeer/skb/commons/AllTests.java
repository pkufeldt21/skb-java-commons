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

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Running all tests for SKB Commons.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.4-SNAPSHOT build 150618 (18-Jun-15) for Java 1.8
 */
public class AllTests {
	/**
	 * Main method for running tests from command line or inside Eclipse
	 * @param args command line arguments
	 */
	@Ignore	public static void main(String[] args) {
		org.junit.runner.JUnitCore.main(ABCSuite.class.getName());
	}

	@RunWith(Suite.class)
	@SuiteClasses({
		//COMMONS
		de.vandermeer.skb.commons.PredicatesTests.class,
		de.vandermeer.skb.commons.TransformersTests.class,

		//COMMONS -> COLLECTIONS
		de.vandermeer.skb.commons.collections.ClassicTableTests.class,
		de.vandermeer.skb.commons.collections.ClassicTreeTests.class,
		de.vandermeer.skb.commons.collections.ComCollectionTests.class,
		de.vandermeer.skb.commons.collections.CompositeTransformersTests.class,
		de.vandermeer.skb.commons.collections.MultiTableTests.class,
		de.vandermeer.skb.commons.collections.MultiTreeTests.class,
		de.vandermeer.skb.commons.collections.PropertyTableTests.class,
		de.vandermeer.skb.commons.collections.TreeTests.class,

		//COMMONS -> UTILS
		de.vandermeer.skb.commons.utils.CLIApacheTests.class,
		de.vandermeer.skb.commons.utils.DictionaryEnumTests.class,
		de.vandermeer.skb.commons.utils.Json2CompositeTests.class,
		de.vandermeer.skb.commons.utils.KVStackTests.class,
		de.vandermeer.skb.commons.utils.ReportManagerTests.class,
		de.vandermeer.skb.commons.utils.SkbContextFactoryTests.class,


	})
	public class ABCSuite {
	}
}
