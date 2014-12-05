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

package de.vandermeer.skb.commons.asciitable;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.vandermeer.skb.asciitable.AsciiTable;
import de.vandermeer.skb.base.Skb_Transformer;
import de.vandermeer.skb.commons.Transformers;

/**
 * Tests for Transformers, some of which are repeated in TransformationTests.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.2 build 140626 (26-Jun-14) with Java 1.8
 */
public class TransformersTests {

	@Test public void test_Object2Text(){
		Skb_Transformer<Object, String> tf=Transformers.OBJECT_TO_TEXT();

		AsciiTable table=AsciiTable.newTable(2, 15);
		table.addRow("col A1", "col A2");
		String test="+------+------+\r\n" +
					"|col A1|col A2|\r\n" + 
					"+------+------+\r\n";
		assertEquals(test, tf.transform(table));
	}
}
