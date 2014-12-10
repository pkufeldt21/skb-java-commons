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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Scanner;

import org.apache.commons.lang3.text.StrBuilder;
import org.junit.Test;

import de.vandermeer.skb.base.Skb_Transformer;
import de.vandermeer.skb.base.message.Message5WH;
import de.vandermeer.skb.base.utils.Skb_UrlUtils;
import de.vandermeer.skb.categories.IsPath;
import de.vandermeer.skb.commons.collections.ComCollection;
import de.vandermeer.skb.commons.collections.FlatMultiTree;
import de.vandermeer.skb.commons.collections.Tree;
import de.vandermeer.skb.composite.CompositeObject;
import de.vandermeer.skb.composite.SimpleObject;
import de.vandermeer.skb.composite.SkbObject;
import de.vandermeer.skb.composite.SpecialObject;
import de.vandermeer.skb.composite.specialobject.SOError;

/**
 * Tests for the JSON to Com conversions.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.3-SNAPSHOT build 141210 (10-Dec-14) for Java 1.8
 */
public class Json2CompositeTests {

	@Test public void testTypes(){
		Json2Collections j2c=new Json2Collections();
		assertFalse(j2c instanceof SkbObject);
		assertFalse(j2c instanceof CompositeObject);
		assertFalse(j2c instanceof SimpleObject);
		assertFalse(j2c instanceof SpecialObject);
	}

	@Test public void testConstructor(){
		Json2Collections jc=new Json2Collections();

		assertNotNull(jc.path);
		assertEquals(0, jc.path.size());
	}

	@Test public void testNullsAndErrors(){
		Json2Collections jc=new Json2Collections();

		assertNull(jc.read((File)null));
		assertNull(jc.read((Scanner)null));
		assertNull(jc.read((URL)null));
		assertNull(jc.read((InputStream)null));
		assertNull(jc.read((Readable)null));

		assertNull(jc.read(new File("foo")));
		try {
			assertNull(jc.read(new URL("file:bar")));
		} catch (MalformedURLException e) {}

		assertNull(jc.s2o(null));		//some test s2o directly
		assertNull(jc.s2o(" "));
		assertNull(jc.s2o("\t"));
		assertNull(jc.s2o("\n"));
		assertNull(jc.s2o("\n\r"));
		assertNull(jc.s2o("\r"));

		assertNull(jc.s2o("eee"));		//illegal JSON string

		assertNull(jc.s2o("{\"level1\"}"));
	}

	@Test public void testJavaLang(){
		Json2Collections jc=new Json2Collections();
		Object obj;

		obj=jc.s2o("20");
		assertTrue(obj instanceof Integer);
		assertEquals(20, obj);

		obj=jc.s2o("20.8");
		assertTrue(obj instanceof Double);
		assertEquals(20.8, obj);

		obj=jc.s2o("3.14159");
		assertTrue(obj instanceof Double);
		assertEquals(3.14159, obj);

		obj=jc.s2o("true");
		assertTrue(obj instanceof Boolean);
		assertEquals(true, obj);

		obj=jc.s2o("\"a string\"");
		assertTrue(obj instanceof String);
		assertEquals("a string", obj);
	}

	@Test public void testComCollection(){
		Json2Collections jc=new Json2Collections();
		Object obj;

		obj=jc.s2o("[]");
		assertTrue(obj instanceof ComCollection);
		assertEquals(0, ((ComCollection<?>)obj).size());

		obj=jc.s2o("[20, 8.20, 3.14159, \"a string\"]");
		assertTrue(obj instanceof ComCollection);
		assertEquals(4, ((ComCollection<?>)obj).size());
		assertTrue(((ComCollection<?>)obj).contains(20));
		assertTrue(((ComCollection<?>)obj).contains(8.20));
		assertTrue(((ComCollection<?>)obj).contains(3.14159));
		assertTrue(((ComCollection<?>)obj).contains("a string"));
	}

	@Test public void testComTree(){
		Json2Collections jc=new Json2Collections();
		Object obj;

		obj=jc.s2o("{}");
		assertTrue(obj instanceof Tree);
		assertTrue(obj instanceof FlatMultiTree);
		assertEquals(0, ((FlatMultiTree<?>)obj).size());	//empty tree size is 0

		obj=jc.s2o("{\"level1\":null}");
		assertTrue(obj instanceof Tree);
		assertTrue(obj instanceof FlatMultiTree);
		assertEquals(2, ((FlatMultiTree<?>)obj).size());
		assertTrue(((FlatMultiTree<?>)obj).containsNode("level1"));

		obj=jc.s2o("{\"key1\":null, \"key2\":[1], \"key3\":10}");
		assertTrue(obj instanceof Tree);
		assertTrue(obj instanceof FlatMultiTree);
		assertEquals(4, ((FlatMultiTree<?>)obj).size());
		assertTrue(((FlatMultiTree<?>)obj).containsNode("key1"));
		assertTrue(((FlatMultiTree<?>)obj).containsNode("key2"));
		assertTrue(((FlatMultiTree<?>)obj).containsNode("key3"));

		obj=jc.s2o("{\"level1\":{\"level2\":{\"level3\":\"value3\"}}}");
		assertTrue(obj instanceof Tree);
		assertTrue(obj instanceof FlatMultiTree);
		assertEquals(4, ((FlatMultiTree<?>)obj).size());
		assertTrue(((FlatMultiTree<?>)obj).containsNode("level1"));
		assertTrue(((FlatMultiTree<?>)obj).containsNode(new Object[]{"level1", "level2"}));
		assertTrue(((FlatMultiTree<?>)obj).containsNode("level1/level2/level3"));

		assertEquals("value3", ((FlatMultiTree<?>)obj).getValue("level1/level2/level3"));

		obj=jc.s2o(
			"{"+
				"\"level1\":{\"key11\":\"value11\", \"key12\":\"value12\","+
					"\"level2\":{\"key21\":\"value21\", \"key22\":\"value22\","+
						"\"level3\":{\"key31\":\"value31\""+
						"}"+
					"}"+
				"}"+
			"}");
		assertTrue(obj instanceof FlatMultiTree);
		assertEquals(9, ((FlatMultiTree<?>)obj).size());
		assertTrue(((FlatMultiTree<?>)obj).containsNode("level1"));
		assertTrue(((FlatMultiTree<?>)obj).containsNode("level1/key11"));
		assertTrue(((FlatMultiTree<?>)obj).containsNode("level1/key12"));
		assertTrue(((FlatMultiTree<?>)obj).containsNode("level1/level2"));
		assertTrue(((FlatMultiTree<?>)obj).containsNode("level1/level2/key21"));
		assertTrue(((FlatMultiTree<?>)obj).containsNode("level1/level2/key22"));
		assertTrue(((FlatMultiTree<?>)obj).containsNode("level1/level2/level3"));
		assertTrue(((FlatMultiTree<?>)obj).containsNode("level1/level2/level3/key31"));
		assertEquals("value31", ((FlatMultiTree<?>)obj).getValue("level1/level2/level3/key31"));

//		System.err.println(((ComMultiTree<?>)obj).size());
//		System.err.println(obj);
	}

	@Test public void testFile(){
//		String filename="de/vandermeer/skb/commons/utils/cli-options.json";
		String filename="de/vandermeer/skb/commons/utils/conversion-map.json";
		URL url=Skb_UrlUtils.getUrl(filename);
		assertNotNull(new SOError().add(new Message5WH().addWhat("error loading file from resource and file system").addHow(filename)).render(), url);

		//read a JSON string from the file, warning if problems with parsing (null) or if return is not a tree (not valid info)
		Object cc=new Json2Collections().read(url);
		assertNotNull(new SOError().add(new Message5WH().addWhat("problem parsing JSON file").addHow(filename)).render(), cc);

		assertTrue(cc instanceof Tree);
		if(!(cc instanceof Tree)){
			System.err.println(new SOError().add(new Message5WH().addWhat("wrong type").addHow("expected tree, found ", cc.getClass().getSimpleName())));
			return;
		}

		@SuppressWarnings("unchecked")
		Tree<Object> tree=(Tree<Object>)cc;

		Skb_Transformer<Object, Integer> count=IsPath.PATH_TO_LEVELS("/");
		Collection<String> names=tree.getChildrenNames("charmap");
		int i;

		i=0;
		for(String s:names){
			if(count.transform(new StrBuilder(s))==1){
				i++;
			}
		}
		assertEquals(0, i);

		i=0;
		for(String s:names){
			if(count.transform(new StrBuilder(s))==2){
				i++;
			}
		}
		assertEquals(1380, i);

		i=0;
		for(String s:names){
			if(count.transform(new StrBuilder(s))==3){
				i++;
			}
		}
		assertEquals(12420, i);

		i=0;
		for(String s:names){
			if(count.transform(new StrBuilder(s))==4){
				i++;
			}
		}
		assertEquals(0, i);
	}
}
