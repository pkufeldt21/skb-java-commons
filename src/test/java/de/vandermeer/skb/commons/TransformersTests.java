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

import static org.junit.Assert.assertEquals;

import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;
import org.stringtemplate.v4.ST;

import de.vandermeer.skb.base.Skb_Transformer;
import de.vandermeer.skb.base.message.Message5WH;
import de.vandermeer.skb.configuration.EPath;
import de.vandermeer.skb.configuration.EPropertyKeys;

/**
 * Tests for Transformers, some of which are repeated in TransformationTests.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.3 build 150618 (18-Jun-15) for Java 1.8
 */
public class TransformersTests {

	@Test public void test_Object2Text(){
		Skb_Transformer<Object, String> tf=Transformers.OBJECT_TO_TEXT();

		assertEquals("", tf.transform(null));
		assertEquals("", tf.transform(""));

		assertEquals("a string", tf.transform("a string"));
		assertEquals("another string", tf.transform("another string"));

		CommonToken tk1=new CommonToken(0);
		assertEquals("", tf.transform(tk1));
		tk1.setText("me Token");
		assertEquals("me Token", tf.transform(tk1));

		ParserRuleContext prc=new ParserRuleContext();
		assertEquals("", tf.transform(prc));
		prc.addChild(tk1);
		assertEquals("me Token", tf.transform(prc));

		ParseTree pt=prc.getChild(0);
		assertEquals("me Token", tf.transform(pt));

		CommonToken tk2=new CommonToken(0);
		tk2.setText(" : and another Token");
		prc.addChild(tk2);
		assertEquals("me Token : and another Token", tf.transform(prc));

		pt=prc.getChild(1);
		assertEquals(" : and another Token", tf.transform(pt));

		ST st=new ST("a and b");
		assertEquals("a and b", tf.transform(st));

		st=new ST("<a> and <b>");
		st.add("a", "Bob");
		st.add("b", "Alice");
		assertEquals("Bob and Alice", tf.transform(st));

		Message5WH msg=new Message5WH();
		assertEquals("", tf.transform(msg));
		msg.setReporter("my Class");
		msg.addWhat("why oh why");
		assertEquals("my Class: >> why oh why", tf.transform(msg));

		assertEquals(EPath.CONFIGURATION.path(), tf.transform(EPath.CONFIGURATION));
		assertEquals(EPropertyKeys.APPLICATION_NAME.key(), tf.transform(EPropertyKeys.APPLICATION_NAME));
	}


}
