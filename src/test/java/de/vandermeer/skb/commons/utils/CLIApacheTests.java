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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.cli.Options;
import org.junit.Test;

import de.vandermeer.skb.base.utils.Skb_UrlUtils;
import de.vandermeer.skb.collections.SetStrategy;
import de.vandermeer.skb.commons.collections.PropertyTable;
import de.vandermeer.skb.commons.collections.Table;
import de.vandermeer.skb.commons.collections.Tree;
import de.vandermeer.skb.composite.specialobject.NOSuccess;
import de.vandermeer.skb.configuration.EPath;

/**
 * Tests for the the Apache CLI implementation.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.4-SNAPSHOT build 150618 (18-Jun-15) for Java 1.8
 */
public class CLIApacheTests {

	@Test public void testDeclareOptionsReal(){
//		Object props=SkbContextFactory.get.defaultProperties();
		PropertyTable cpt=SkbContextFactory.get.newPropertyTable(SkbContextFactory.get.defaultProperties());

		CLIApache aca=new CLIApache();
		aca.declareOptions(cpt);
		assertEquals(8, aca.options.getOptions().size()); //note: if default-config.json changes number change here, too!
	}

	@Test public void testDeclareOptions(){
		CLIApache aca=new CLIApache();
		aca.declareOptions(this.prepareComProperties());

		assertEquals(6, aca.optionList.size());
		for(String s:this.rows()){
			assertTrue(aca.optionList.containsKey(Table.defaulSeparator+s));
		}

		Options op=aca.options;

		assertEquals(6, op.getOptions().size());

		assertTrue(op.hasOption("-b"));
		assertTrue(op.hasOption("-d"));
		assertTrue(op.hasOption("-i"));
		assertTrue(op.hasOption("-l"));
		assertTrue(op.hasOption("-s"));

		assertTrue(op.hasOption("--test-boolean"));
		assertTrue(op.hasOption("--test-double"));
		assertTrue(op.hasOption("--test-integer"));
		assertTrue(op.hasOption("--test-long"));
		assertTrue(op.hasOption("--test-string"));

		assertTrue(op.hasOption("--test-string-w-arg"));
		assertTrue(op.getOption("--test-string-w-arg").hasArg());
		assertTrue(op.getOption("--test-string-w-arg").hasArgName());
	}

	@Test public void testParse(){
		CLIApache aca=new CLIApache();
		aca.declareOptions(this.prepareComProperties());

		assertNotSame("", aca.parse(new String[]{"-x"}, false));	//stop at parse error
		assertSame("", aca.parse(new String[]{"-x"}, true));		//don't stop at parse error
		assertSame("", aca.parse(new String[]{"-b", "-d", "-i", "-l", "-s"}, false));

		assertSame("", aca.parse(new String[]{"--test-boolean"}, false));
		assertSame("", aca.parse(new String[]{"--test-double"}, false));
		assertSame("", aca.parse(new String[]{"--test-integer"}, true));
		assertSame("", aca.parse(new String[]{"--test-long"}, true));
		assertSame("", aca.parse(new String[]{"--test-string"}, true));
		assertSame("", aca.parse(new String[]{"--test-string-w-arg", "ar"}, true));
	}

	@Test public void testGetOptions(){
		PropertyTable cp=this.prepareComProperties();

		CLIApache aca=new CLIApache();
		aca.declareOptions(cp);

		assertSame("", aca.parse(new String[]{"-b", "-d", "-i", "-l", "-s", "--test-boolean", "--test-double", "--test-integer", "--test-long", "--test-string", "--test-string-w-arg", "ar"}, false));
		aca.getOptions(cp);
		//TODO test that
	}

	private PropertyTable prepareComProperties(){
		URL url=Skb_UrlUtils.getUrl("de/vandermeer/skb/commons/utils/cli-options.json");
		assertNotNull(url);

		Object cc=new Json2Collections().read(url);
		assertNotNull(cc);
		assertTrue(cc instanceof Tree);

		Tree<?> tree=((Tree<?>)cc).getSubtree(EPath.CONFIGURATION.path());
		assertNotNull(tree);

		PropertyTable cp=new PropertyTable(SetStrategy.HASH_SET);
		cp.addRowsAll(this.rows());

		assertEquals(NOSuccess.get, cp.loadFromTree(EPath.CONFIGURATION, this.rows(), tree));
		return cp;
	}

	private Collection<String> rows(){
		String[] rows=new String[]{"testoption.boolean", "testoption.double", "testoption.integer", "testoption.long", "testoption.string", "testoption.string-w-arg"};
		return Arrays.asList(rows);
	}
}
