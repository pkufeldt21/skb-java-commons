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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

import de.vandermeer.skb.categories.kvt.IsAttributeKey;
import de.vandermeer.skb.collections.ListStrategy;
import de.vandermeer.skb.collections.SetStrategy;
import de.vandermeer.skb.commons.collections.FlatMultiTable;
import de.vandermeer.skb.commons.collections.Table;
import de.vandermeer.skb.composite.CompositeObject;
import de.vandermeer.skb.composite.SimpleObject;
import de.vandermeer.skb.composite.SkbObject;
import de.vandermeer.skb.composite.SpecialObject;
import de.vandermeer.skb.configuration.EAttributeKeys;

/**
 * Tests for multi table.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.3-SNAPSHOT build 141210 (10-Dec-14) for Java 1.8
 */
public class MultiTableTests {

	@Test public void testTypes(){
		SkbObject t;
		t=new FlatMultiTable<Object>();
		assertTrue(t instanceof Table);
		assertTrue(t instanceof CompositeObject);
		assertFalse(t instanceof SimpleObject);
		assertFalse(t instanceof SpecialObject);
	}

	@Test public void testSettings(){
		FlatMultiTable<Object> table=new FlatMultiTable<>();
		assertTrue(table.autoClean);
		assertTrue(table.autoRoot);
		assertFalse(table.autoRootArray);
	}

	private void constructorAll(FlatMultiTable<?> table){
		assertNotNull(table.sval);
		assertEquals(0, table.sval.size());			// table is empty
		assertEquals(null, table.get("/", null));	// nothing in the table

		//testing keys and empty. keys is not empty (root node) but table should say it is empty
		assertTrue(table.keys().isEmpty());		//sval is empty
		assertTrue(table.isEmpty());			//table is empty

		assertNotNull(table.columns);
	}

	@Test public void testAddRow(){
		IsAttributeKey[] c=new IsAttributeKey[]{IsAttributeKey.create("col1"), IsAttributeKey.create("col2"), IsAttributeKey.create("col3")};
		FlatMultiTable<String> table=new FlatMultiTable<String>(SetStrategy.HASH_SET, c);

		assertTrue(table.addRow("/test1"));
		assertTrue(table.contains("test1"));
		assertTrue(table.contains("test1", "col1"));
		assertTrue(table.contains("test1", "col2"));
		assertTrue(table.contains("test1", "col3"));

		assertFalse(table.addRow("t2/illegal"));
		assertFalse(table.contains("t2"));
		assertFalse(table.contains("t2", "col1"));

		assertTrue(table.addRow("legal"));
		assertTrue(table.contains("legal"));
		assertTrue(table.contains("legal", "col1"));
		assertTrue(table.contains("legal", "col2"));
		assertTrue(table.contains("legal", "col3"));

		assertTrue(table.addRow("///legal-plus"));
		assertTrue(table.contains("/legal-plus"));
		assertTrue(table.contains("/legal-plus", "col1"));
		assertTrue(table.contains("/legal-plus", "col2"));
		assertTrue(table.contains("/legal-plus", "col3"));

		assertFalse(table.addRow(null));
	}

	@Test public void testAddRowsAll(){	//for Object[] and Collection<?>
		IsAttributeKey[] c=new IsAttributeKey[]{IsAttributeKey.create("col1"), IsAttributeKey.create("col2"), IsAttributeKey.create("col3")};
		FlatMultiTable<String> table=new FlatMultiTable<String>(SetStrategy.HASH_SET, c);

		String[] rowsStar=new String[]{"r1", "r2", null, "illegal/row"};
		Object[] rowsObar=new Object[]{"r4", null, ",ill/row", "r5"};
		Set<String> set=SetStrategy.LINKED_HASH_SET.get(String.class);
		set.add("r6");
		set.add("r7");
		List<String> list=ListStrategy.STACK.get(String.class);
		list.add("r8");
		list.add("r9");
		list.add(null);

		assertTrue(table.addRowsAll(rowsStar));
		assertEquals(8, table.size());
		assertTrue(table.contains("r1"));
		assertTrue(table.contains("r1/col1"));
		assertTrue(table.contains("r2"));
		assertTrue(table.contains("r2/col2"));

		assertTrue(table.addRowsAll(rowsObar));
		assertEquals(16, table.size());
		assertTrue(table.contains("r4"));
		assertTrue(table.contains("r4/col1"));
		assertTrue(table.contains("r5"));
		assertTrue(table.contains("r5/col2"));

		assertTrue(table.addRowsAll(set));
		assertEquals(24, table.size());
		assertTrue(table.contains("r6"));
		assertTrue(table.contains("r6/col1"));
		assertTrue(table.contains("r7"));
		assertTrue(table.contains("r7/col2"));

		assertTrue(table.addRowsAll(list));
		assertEquals(32, table.size());
		assertTrue(table.contains("r8"));
		assertTrue(table.contains("r8/col1"));
		assertTrue(table.contains("r9"));
		assertTrue(table.contains("r9/col2"));

		assertFalse(table.addRowsAll(null));
		assertEquals(32, table.size());

		assertFalse(table.addRowsAll("notAnArray"));
	}

	@Test public void testConstructorArray(){
		FlatMultiTable<String> table=new FlatMultiTable<String>(SetStrategy.LINKED_HASH_SET, (IsAttributeKey[])null);
		this.constructorAll(table);
		assertEquals(SetStrategy.LINKED_HASH_SET, table.strategy);

		assertEquals(1, table.columns.size());
		assertEquals(EAttributeKeys.DEFAULT, table.columns.iterator().next());

		IsAttributeKey[] c=new IsAttributeKey[]{EAttributeKeys.CLI_PARAMETER_ARGUMENTS, EAttributeKeys.VALUE_CLI, EAttributeKeys.VALUE_TYPE};
		table=new FlatMultiTable<String>(SetStrategy.HASH_SET, c);
		this.constructorAll(table);
		assertEquals(SetStrategy.HASH_SET, table.strategy);

		assertEquals(3, table.columns.size());
		assertTrue(table.columns.contains(EAttributeKeys.CLI_PARAMETER_ARGUMENTS));
		assertTrue(table.columns.contains(EAttributeKeys.VALUE_CLI));
		assertTrue(table.columns.contains(EAttributeKeys.VALUE_TYPE));
	}

	@Test public void testConstructorCollection(){
		FlatMultiTable<String> table=new FlatMultiTable<String>(SetStrategy.LINKED_HASH_SET, (Collection<IsAttributeKey>)null);
		this.constructorAll(table);
		assertEquals(SetStrategy.LINKED_HASH_SET, table.strategy);

		assertEquals(1, table.columns.size());
		assertEquals(EAttributeKeys.DEFAULT, table.columns.iterator().next());

		Set<IsAttributeKey> c=new TreeSet<IsAttributeKey>(IsAttributeKey.comparator);
		c.add(EAttributeKeys.CLI_PARAMETER_ARGUMENTS);
		c.add(EAttributeKeys.VALUE_CLI);
		c.add(EAttributeKeys.VALUE_TYPE);
		table=new FlatMultiTable<String>(SetStrategy.HASH_SET, c);
		this.constructorAll(table);
		assertEquals(SetStrategy.HASH_SET, table.strategy);

		assertEquals(3, table.columns.size());
		assertTrue(table.columns.contains(EAttributeKeys.CLI_PARAMETER_ARGUMENTS));
		assertTrue(table.columns.contains(EAttributeKeys.VALUE_CLI));
		assertTrue(table.columns.contains(EAttributeKeys.VALUE_TYPE));
	}

	@Test public void testConstructorPlain(){
		FlatMultiTable<String> table=new FlatMultiTable<String>();

		this.constructorAll(table);
		assertEquals(SetStrategy.LINKED_HASH_SET, table.strategy);

		assertEquals(1, table.columns.size());
		assertEquals(EAttributeKeys.DEFAULT, table.columns.iterator().next());
	}

	@Test public void testContains(){
		FlatMultiTable<Integer> table=new FlatMultiTable<Integer>();

		table.sval.put("/k1", null);
		table.sval.put("/k1/l2", null);
		table.sval.put("/k1/l2/l3", null);	//level 3 is illegal in table, but we only test contains here

		table.sval.put("k2", null);
		table.sval.put("k2/l2", null);
		table.sval.put("k2/l2/l3", null);

		assertTrue(table.contains("/k1"));
		assertTrue(table.contains("k1"));
		assertTrue(table.contains("/k1/l2"));
		assertTrue(table.contains("k1/l2"));
		assertTrue(table.contains("/k1/l2/l3"));
		assertTrue(table.contains("k1/l2/l3"));

		assertFalse(table.contains("/k2"));			//have been inserted w/o leading separator
		assertFalse(table.contains("k2"));
		assertFalse(table.contains("/k2/l2"));
		assertFalse(table.contains("k2/l2"));
		assertFalse(table.contains("/k2/l2/l3"));
		assertFalse(table.contains("k2/l2/l3"));

		assertTrue(table.contains("/", "k1"));
		assertTrue(table.contains(null, "k1"));
		assertTrue(table.contains("/k1", "/l2"));
		assertTrue(table.contains("k1", "/l2"));
		assertTrue(table.contains("/k1/", "l2/l3"));
		assertTrue(table.contains("k1/l2/", "l3"));

		assertFalse(table.contains(null));
		assertFalse(table.contains(null, null));
	}

	@Test public void testSizeKeysEmptyClear(){
		FlatMultiTable<String> table=new FlatMultiTable<String>();

		assertTrue(table.isEmpty());
		assertEquals(0, table.keys().size());
		assertEquals(0, table.size());

		table.sval.put("key1", null);
		assertFalse(table.isEmpty());
		assertEquals(1, table.keys().size());
		assertEquals(1, table.size());

		table.sval.put("key2", null);
		assertFalse(table.isEmpty());
		assertEquals(2, table.keys().size());
		assertEquals(2, table.size());

		table.sval.put("key2", null);
		assertFalse(table.isEmpty());
		assertEquals(2, table.keys().size());
		assertEquals(2, table.size());

		table.clear();
		assertTrue(table.isEmpty());
		assertEquals(0, table.keys().size());
		assertEquals(0, table.size());
	}
}
