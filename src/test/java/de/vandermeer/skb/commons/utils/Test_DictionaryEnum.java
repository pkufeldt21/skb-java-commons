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
import static org.junit.Assert.assertNull;

import org.junit.Test;

import de.vandermeer.skb.base.categories.IsDictionary;
import de.vandermeer.skb.configuration.ETypeMap;

/**
 * Tests for the DictionaryEnum implementation of {@link IsDictionary}.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.4 build 150701 (01-Jul-15) for Java 1.8
 */
public class Test_DictionaryEnum {
	DictionaryEnum<String, Class<?>, ETypeMap> typeMap=new DictionaryEnum<String, Class<?>, ETypeMap>(ETypeMap.values());

	@Test public void testGetForString(){
		assertEquals(ETypeMap.JAVA_STRING,  typeMap.getPair4Source("skb.type.string"));
		assertEquals(ETypeMap.JAVA_LONG,    typeMap.getPair4Source("skb.type.long"));
		assertEquals(ETypeMap.JAVA_SHORT,   typeMap.getPair4Source("skb.type.short"));
		assertEquals(ETypeMap.JAVA_INTEGER, typeMap.getPair4Source("skb.type.integer"));
		assertEquals(ETypeMap.JAVA_FLOAT,   typeMap.getPair4Source("skb.type.float"));
		assertEquals(ETypeMap.JAVA_DOUBLE,  typeMap.getPair4Source("skb.type.double"));
		assertEquals(ETypeMap.JAVA_BYTE,    typeMap.getPair4Source("skb.type.byte"));
		assertEquals(ETypeMap.JAVA_BOOLEAN, typeMap.getPair4Source("skb.type.boolean"));

		assertNull(typeMap.getPair4Source("somethings"));
		assertNull(typeMap.getPair4Source(null));
	}

	@Test public final void testGetClass(){
		assertEquals(String.class,  typeMap.toTarget("skb.type.string"));
		assertEquals(Long.class,    typeMap.toTarget("skb.type.long"));
		assertEquals(Short.class,   typeMap.toTarget("skb.type.short"));
		assertEquals(Integer.class, typeMap.toTarget("skb.type.integer"));
		assertEquals(Float.class,   typeMap.toTarget("skb.type.float"));
		assertEquals(Double.class,  typeMap.toTarget("skb.type.double"));
		assertEquals(Byte.class,    typeMap.toTarget("skb.type.byte"));
		assertEquals(Boolean.class, typeMap.toTarget("skb.type.boolean"));

		assertNull(typeMap.toTarget("somethings"));
		assertNull(typeMap.toTarget(null));
	}

	@Test public void testGetForClass(){
		assertEquals(ETypeMap.JAVA_STRING,  typeMap.getPair4Target(String.class));
		assertEquals(ETypeMap.JAVA_LONG,    typeMap.getPair4Target(Long.class));
		assertEquals(ETypeMap.JAVA_SHORT,   typeMap.getPair4Target(Short.class));
		assertEquals(ETypeMap.JAVA_INTEGER, typeMap.getPair4Target(Integer.class));
		assertEquals(ETypeMap.JAVA_FLOAT,   typeMap.getPair4Target(Float.class));
		assertEquals(ETypeMap.JAVA_DOUBLE,  typeMap.getPair4Target(Double.class));
		assertEquals(ETypeMap.JAVA_BYTE,    typeMap.getPair4Target(Byte.class));
		assertEquals(ETypeMap.JAVA_BOOLEAN, typeMap.getPair4Target(Boolean.class));

		assertEquals(ETypeMap.UNKNOWN, typeMap.getPair4Target(Object.class));
		assertNull(typeMap.getPair4Target(this.getClass()));
		assertNull(typeMap.getPair4Target((Class<?>)null));
	}

	@Test public void testGetString(){
		assertEquals("skb.type.string",  typeMap.toSource(String.class));
		assertEquals("skb.type.long",    typeMap.toSource(Long.class));
		assertEquals("skb.type.short",   typeMap.toSource(Short.class));
		assertEquals("skb.type.integer", typeMap.toSource(Integer.class));
		assertEquals("skb.type.float",   typeMap.toSource(Float.class));
		assertEquals("skb.type.double",  typeMap.toSource(Double.class));
		assertEquals("skb.type.byte",    typeMap.toSource(Byte.class));
		assertEquals("skb.type.boolean", typeMap.toSource(Boolean.class));

		assertEquals("__unknown__", typeMap.toSource(Object.class));
		assertNull(typeMap.toSource(this.getClass()));
		assertNull(typeMap.toSource((Class<?>)null));
	}
}
