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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.vandermeer.skb.base.composite.Com_Coin;
import de.vandermeer.skb.base.tools.ReportManager;
import de.vandermeer.skb.commons.collections.PropertyTable;
import de.vandermeer.skb.commons.collections.Tree;

/**
 * Tests for the context factory.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.4 build 150701 (01-Jul-15) for Java 1.8
 */
public class Test_SkbContextFactory {

	@Test public void testGetDefaultProperties(){
		Object props=SkbContextFactory.get.defaultProperties();
		assertFalse(props instanceof Com_Coin);		//if true, SO holds problem
		assertTrue(props instanceof Tree);					//if ComTree we are ok
	}

	@Test public void testGetPropertyTable(){
//		Object props=SkbContextFactory.get.defaultProperties();
		PropertyTable cpt=SkbContextFactory.get.newPropertyTable(SkbContextFactory.get.defaultProperties());
		assertNotNull(cpt);
	}

	@Test public void testGetPropertyTableNull(){
		PropertyTable cpt=SkbContextFactory.get.newPropertyTable(null);
		assertNotNull(cpt);
	}

	@Test public void testGetReportManager(){
//		Object props=SkbContextFactory.get.defaultProperties();
		PropertyTable cpt=SkbContextFactory.get.newPropertyTable(SkbContextFactory.get.defaultProperties());
		ReportManager arm=SkbContextFactory.get.newReportManager(cpt);
		assertNotNull(arm);
		assertTrue(arm.isLoaded());	//if not, then getInitErrors will hold the problem

		//test for null
		arm=SkbContextFactory.get.newReportManager(null);
		assertNull(arm);
	}

	@Test public void testGetReportManagerNull(){
		ReportManager arm=SkbContextFactory.get.newReportManager(null);
		assertNull(arm);
	}
}
