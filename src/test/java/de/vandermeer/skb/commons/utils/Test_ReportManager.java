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
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupString;

import de.vandermeer.skb.base.message.EMessageType;
import de.vandermeer.skb.base.message.Message5WH;
import de.vandermeer.skb.composite.specialobject.SOError;
import de.vandermeer.skb.composite.specialobject.SOWarning;

/**
 * Tests for the AtcReportManager.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.4 build 150619 (19-Jun-15) for Java 1.8
 */
public class Test_ReportManager {

	@Test public void testDoubleCheckStatics(){
		assertTrue(ReportManager.stgChunks.containsKey("report"));
		assertTrue(ReportManager.stgChunks.get("report").contains("who"));
		assertTrue(ReportManager.stgChunks.get("report").contains("what"));
		assertTrue(ReportManager.stgChunks.get("report").contains("when"));
		assertTrue(ReportManager.stgChunks.get("report").contains("where"));
		assertTrue(ReportManager.stgChunks.get("report").contains("who"));
		assertTrue(ReportManager.stgChunks.get("report").contains("how"));
		assertTrue(ReportManager.stgChunks.get("report").contains("type"));
		assertTrue(ReportManager.stgChunks.get("report").contains("reporter"));

		assertTrue(ReportManager.stgChunks.containsKey("where"));
		assertTrue(ReportManager.stgChunks.get("where").contains("location"));
		assertTrue(ReportManager.stgChunks.get("where").contains("line"));
		assertTrue(ReportManager.stgChunks.get("where").contains("column"));

		assertTrue(ReportManager.stgChunks.containsKey("maxErrors"));
		assertTrue(ReportManager.stgChunks.get("maxErrors").contains("name"));
		assertTrue(ReportManager.stgChunks.get("maxErrors").contains("number"));
	}

	@Test public void testConstructors(){
		ReportManager arm;

		//test with default STG in resources
		arm=new ReportManager("de/vandermeer/skb/commons/report-manager.stg", "myTest");
		this.checkStandardInitialValues(arm, 100);
		assertNotNull(arm.stg);
		assertEquals(0, arm.errorList.size());
		assertTrue(arm.isLoaded());

		//test default with different maxError
		arm=new ReportManager("de/vandermeer/skb/commons/report-manager.stg", 99, "myTest");
		this.checkStandardInitialValues(arm, 99);
		assertNotNull(arm.stg);
		assertEquals(0, arm.errorList.size());
		assertTrue(arm.isLoaded());

		String stString=
				"report(who, what, when, where, why, how, type, reporter) ::= << \n"+
				">> \n\n"+
				"where(location, line, column) ::= << \n"+
				">> \n\n"+
				"maxErrors(name, number) ::= << \n"+
				">> \n\n";
		STGroup stg=new STGroupString(stString);

		arm=new ReportManager(stg, "me test");
		this.checkStandardInitialValues(arm, 100);
		assertNotNull(arm.stg);
		assertEquals(0, arm.errorList.size());
		assertTrue(arm.isLoaded());

		arm=new ReportManager(stg, 49, "me test");
		this.checkStandardInitialValues(arm, 49);
		assertNotNull(arm.stg);
		assertEquals(0, arm.errorList.size());
		assertTrue(arm.isLoaded());

		stString=
			"report() ::= << \n"+
			">> \n\n"+
			"where(location, line, column) ::= << \n"+
			">> \n\n"+
			"maxErrors(name, number) ::= << \n"+
			">> \n\n";
		stg=new STGroupString(stString);

		arm=new ReportManager(stg, "me test");
		assertEquals(0, arm.reportTypes.size());
		assertNotNull(arm.stg);
		assertEquals(8, arm.errorList.size());
		assertFalse(arm.isLoaded());

		stString=
			"report() ::= << \n"+
			">> \n\n"+
			"maxErrors(name, number) ::= << \n"+
			">> \n\n";
		stg=new STGroupString(stString);

		arm=new ReportManager(stg, 29, "me test");
		assertEquals(0, arm.reportTypes.size());
		assertEquals(29, arm.maxErrors);
		assertNotNull(arm.stg);
		assertEquals(9, arm.errorList.size());
		assertFalse(arm.isLoaded());
	}

	private void checkStandardInitialValues(ReportManager arm, int expectedMaxErr){
		assertEquals(3, arm.counters.length);
		assertEquals(0, arm.counters[0]);
		assertEquals(0, arm.counters[1]);
		assertEquals(0, arm.counters[2]);

		assertEquals(0, arm.getMessageCount(EMessageType.ERROR));
		assertEquals(0, arm.getMessageCount(EMessageType.WARNING));
		assertEquals(0, arm.getMessageCount(EMessageType.INFO));

		EMessageType[] types=EMessageType.values();
		assertEquals(types.length, arm.reportTypes.size());
		for(EMessageType t:types){
			assertTrue(arm.reportTypes.contains(t));
		}

		assertEquals(expectedMaxErr, arm.maxErrors);
	}

	@Test public void testConfigureReporting(){
		ReportManager arm=new ReportManager("de/vandermeer/skb/commons/report-manager.stg", "myTest");

		arm.configureReporting(Arrays.asList(new EMessageType[]{EMessageType.ERROR}));
		assertTrue(arm.isEnabledFor(EMessageType.ERROR));
		assertFalse(arm.isEnabledFor(EMessageType.WARNING));
		assertFalse(arm.isEnabledFor(EMessageType.INFO));

		arm.configureReporting(Arrays.asList(new EMessageType[]{EMessageType.WARNING, EMessageType.INFO}));
		assertFalse(arm.isEnabledFor(EMessageType.ERROR));
		assertTrue(arm.isEnabledFor(EMessageType.WARNING));
		assertTrue(arm.isEnabledFor(EMessageType.INFO));
	}

	@Test public void testReportDirect(){
		ReportManager arm=new ReportManager("de/vandermeer/skb/commons/report-manager.stg", "myTest");

		Message5WH msg=new Message5WH().setReporter("testReportDirect").setWho("junit").addWhat("a test message (ignore)");

		msg.setType(EMessageType.ERROR);
		assertFalse(arm.report(null));
		assertEquals(0, arm.getMessageCount(EMessageType.ERROR));
		assertTrue(arm.report(msg));
		assertEquals(1, arm.getMessageCount(EMessageType.ERROR));
		assertTrue(arm.report(msg));
		assertEquals(2, arm.getMessageCount(EMessageType.ERROR));

		msg.setType(EMessageType.WARNING);
		assertFalse(arm.report(null));
		assertEquals(0, arm.getMessageCount(EMessageType.WARNING));
		assertTrue(arm.report(msg));
		assertEquals(1, arm.getMessageCount(EMessageType.WARNING));
		assertTrue(arm.report(msg));
		assertEquals(2, arm.getMessageCount(EMessageType.WARNING));

		msg.setType(EMessageType.INFO);
		assertFalse(arm.report(null));
		assertEquals(0, arm.getMessageCount(EMessageType.INFO));
		assertTrue(arm.report(msg));
		assertEquals(1, arm.getMessageCount(EMessageType.INFO));
		assertTrue(arm.report(msg));
		assertEquals(2, arm.getMessageCount(EMessageType.INFO));

		arm.resetMessageCount(EMessageType.ERROR);
		assertEquals(0, arm.getMessageCount(EMessageType.ERROR));
		arm.resetMessageCount(EMessageType.WARNING);
		assertEquals(0, arm.getMessageCount(EMessageType.WARNING));
		arm.resetMessageCount(EMessageType.INFO);
		assertEquals(0, arm.getMessageCount(EMessageType.INFO));
	}

	@Test public void testReportPublic(){
		ReportManager arm=new ReportManager("de/vandermeer/skb/commons/report-manager.stg", "myTest");

		Message5WH msg=new Message5WH().setReporter("testReportDirect").setWho("junit").addWhat("a test message (ignore)");

		SOError err=new SOError().add(msg).add(msg);
		assertTrue(arm.report(err));
		assertEquals(2, arm.getMessageCount(EMessageType.ERROR));

		SOWarning warn=new SOWarning().add(msg).add(msg);
		assertTrue(arm.report(warn));
		assertEquals(2, arm.getMessageCount(EMessageType.WARNING));

		msg.setType(null);
		assertFalse(arm.report(msg));

		msg.setType(EMessageType.INFO);
		Object[] oarr=new Object[]{msg, msg, msg};
		assertTrue(arm.report(oarr));
		assertEquals(3, arm.getMessageCount(EMessageType.INFO));

		HashSet<Object> set=new HashSet<Object>();
		msg.setType(EMessageType.WARNING);
		set.add(msg);
		assertTrue(arm.report(set));
		assertEquals(3, arm.getMessageCount(EMessageType.WARNING));

		arm.resetMessageCount(EMessageType.ERROR);
		assertEquals(0, arm.getMessageCount(EMessageType.ERROR));
		arm.resetMessageCount(EMessageType.WARNING);
		assertEquals(0, arm.getMessageCount(EMessageType.WARNING));
		arm.resetMessageCount(EMessageType.INFO);
		assertEquals(0, arm.getMessageCount(EMessageType.INFO));
	}
}
