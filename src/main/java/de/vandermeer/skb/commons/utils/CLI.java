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

import de.vandermeer.skb.base.composite.Com_Coin;
import de.vandermeer.skb.base.composite.coin.CC_Warning;
import de.vandermeer.skb.base.composite.coin.NOSuccess;
import de.vandermeer.skb.commons.collections.PropertyTable;

/**
 * A Command Line Interface (command line parser).
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.4 build 150701 (01-Jul-15) for Java 1.8
 */
public interface CLI {
	/**
	 * Parses the command line.
	 * All parsed options will be stored locally. Use {@link CLI#getOptions(PropertyTable)} to retrieve the result.
	 * @param args arguments from the main method
	 * @param stopAtNonOption true if a non-option should cause termination, false otherwise
	 * @return empty string on success, string with errors (exception trace) otherwise
	 */
	String parse(String[] args, boolean stopAtNonOption);

	/**
	 * Sets options in the property map with the values parsed from the command line.
	 * This method can be called after parsing the command line.
	 * @param prop map for options
	 * @return {@link NOSuccess} on success, {@link CC_Warning} with more information if any problem occurred
	 */
	Com_Coin getOptions(PropertyTable prop);

	/**
	 * Declares command line options with their arguments.
	 * This method should be called before parsing the command line.
	 * @param prop property map with command line options
	 * @return {@link NOSuccess} on success, {@link CC_Warning} with more information if any problem occurred
	 */
	Com_Coin declareOptions(PropertyTable prop);

	CLI getCopy();

	/**
	 * Returns a usage screen.
	 * @param header information printed before auto-generated usage
	 * @param footer information printed after auto-generated usage
	 * @param width maximum width of a line
	 * @param autoUsage switch auto-generates usage on/off
	 * @param applicationName name of the application
	 * @return string with the usage screen
	 */
	String usage(String header, String footer, int width, boolean autoUsage, Object applicationName);
}
