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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.lang3.StringUtils;

import de.vandermeer.skb.base.composite.Com_Coin;
import de.vandermeer.skb.base.composite.coin.CC_Warning;
import de.vandermeer.skb.base.composite.coin.NONone;
import de.vandermeer.skb.base.composite.coin.NONull;
import de.vandermeer.skb.base.composite.coin.NOSuccess;
import de.vandermeer.skb.base.message.Message5WH_Builder;
import de.vandermeer.skb.base.utils.Skb_ObjectUtils;
import de.vandermeer.skb.commons.collections.PropertyTable;
import de.vandermeer.skb.configuration.EAttributeKeys;
import de.vandermeer.skb.configuration.ETypeMap;

/**
 * Implementation of the {@link CLI} interface using Apache Commons CLI.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.4 build 150701 (01-Jul-15) for Java 1.8
 */
public class CLIApache implements CLI {

	/** The command line options. */
	protected Options options;

	/** The command line parsed. */
	protected CommandLine cmdLine;

	/** List of options. */
	protected LinkedHashMap<String, String> optionList;

	/** Type map as dictionary to translate defined strings into Java objects. */
	protected final DictionaryEnum<String, Class<?>, ETypeMap> typeMap = new DictionaryEnum<String, Class<?>, ETypeMap>(ETypeMap.values());

	public CLIApache(){
		this.options = new Options();
		this.optionList = new LinkedHashMap<String, String>();
	}

	@Override
	public String parse(String[] args, boolean stopAtNonOption) {
		String ret = "";
		CommandLineParser parser = new PosixParser();
		try {
			this.cmdLine = parser.parse(this.options, args, stopAtNonOption);
		}
		catch(ParseException exp){
			ret = StringUtils.removeStart(exp.toString(), "org.apache.commons.cli.UnrecognizedOptionException: ");
		}
		return ret;
	}

	@Override
	public Com_Coin getOptions(PropertyTable prop) {
		CC_Warning ret = null;
		String val;
		for(String key : this.optionList.keySet()){
			val = this.optionList.get(key);
			if(this.cmdLine.hasOption(val)){

				switch(this.typeMap.getPair4Source(prop.get(key, EAttributeKeys.CLI_PARAMETER_TYPE).toString())){
					case JAVA_STRING:
						prop.setPropertyValueCli(key, new String(cmdLine.getOptionValue(val, "")));
						break;
					case JAVA_BOOLEAN:
						prop.setPropertyValueCli(key, new Boolean(true));
						break;
					case JAVA_INTEGER:
						Integer i = Skb_ObjectUtils.CONVERT(cmdLine.getOptionValue(val), Integer.class, new Integer(0), new Integer(0));
						prop.setPropertyValueCli(key, i);
						break;
					case JAVA_DOUBLE:
						Double d = Skb_ObjectUtils.CONVERT(cmdLine.getOptionValue(val), Double.class, 0.0, 0.0);
						prop.setPropertyValueCli(key, d);
						break;
					case JAVA_LONG:
						Long l = Skb_ObjectUtils.CONVERT(cmdLine.getOptionValue(val), Long.class, new Long(0), new Long(0));
						prop.setPropertyValueCli(key, l);
						break;
					default:
						if(ret==null){
							ret = new CC_Warning();
						}
						ret.add(new Message5WH_Builder().addWhat("unknown type <").addWhat(prop.get(key, EAttributeKeys.CLI_PARAMETER_TYPE)).addWhat("> for <").addWhat(key).addWhat(">").build());
				}

			}
		}
		if(ret==null){
			return NOSuccess.get;
		}
		return ret;
	}

	@Override
	public Com_Coin declareOptions(PropertyTable prop) {
		String optShort;
		String optLong;
		CC_Warning ret = null;

		for (String current : prop.keys()){
			if(prop.hasPropertyValue(current, EAttributeKeys.CLI_PARAMETER_TYPE)){
				Object o = Skb_ObjectUtils.CONVERT(prop.get(current, EAttributeKeys.CLI_PARAMETER_LONG), Object.class, NONull.get, NONone.get);
				if(!(o instanceof Com_Coin)){
					optLong = o.toString();
				}
				else{
					optLong = null;
				}
				OptionBuilder.withLongOpt(optLong);

				o = Skb_ObjectUtils.CONVERT(prop.get(current, EAttributeKeys.CLI_PARAMETER_DESCRIPTION_SHORT), Object.class, NONull.get, NONone.get);
				OptionBuilder.withDescription(o.toString());

				o = Skb_ObjectUtils.CONVERT(prop.get(current, EAttributeKeys.CLI_PARAMETER_DESCRIPTION_ARGUMENTS), Object.class, NONull.get, NONone.get);
				if(!(o instanceof Com_Coin) && o.toString().length()>0){
					OptionBuilder.hasArg();
					OptionBuilder.withArgName(o.toString());
				}
				else{
					OptionBuilder.hasArg(false);
				}

				switch(this.typeMap.getPair4Source(prop.get(current, EAttributeKeys.CLI_PARAMETER_TYPE).toString())){
					case JAVA_BOOLEAN:
						OptionBuilder.withType(Boolean.class);
						break;
					case JAVA_DOUBLE:
						OptionBuilder.withType(Double.class);
						break;
					case JAVA_INTEGER:
						OptionBuilder.withType(Integer.class);
						break;
					case JAVA_LONG:
						OptionBuilder.withType(Long.class);
						break;
					case JAVA_STRING:
					default:
						OptionBuilder.withType(String.class);
						break;
				}


				o = prop.get(current, EAttributeKeys.CLI_PARAMETER_SHORT);
				if(o!=null && !(o instanceof Com_Coin)){
					optShort = o.toString();
				}
				else{
					optShort = null;
				}

				if(optShort!=null && optLong!=null){
					this.options.addOption(OptionBuilder.create(optShort.charAt(0)));
					this.optionList.put(current, optLong);
				}
				else if(optLong!=null){
					this.options.addOption(OptionBuilder.create());
					this.optionList.put(current, optLong);
				}
				else{
					//dummy create, nothing to be done since no option set (short/long)
					OptionBuilder.withLongOpt("__dummyLongOpt__");
					OptionBuilder.create();

					if(ret==null){
						ret = new CC_Warning();
					}
					ret.add(new Message5WH_Builder().addWhat("no short and no long options for <").addWhat(current).addWhat(">").build());
				}
			}
		}

		if(ret==null){
			return NOSuccess.get;
		}
		return ret;
	}

	@Override
	public CLI getCopy() {
		return this;
	}

	@Override
	public String toString() {
		return "CLI Apache";
	}

	@Override
	public String usage(String header, String footer, int width, boolean autoUsage, Object applicationName) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		String appName = "";
		if(applicationName!=null){
			appName=applicationName.toString();
		}

		HelpFormatter formatter = new HelpFormatter();
		//formatter.printWrapped(pw, width, header); // formatter didn't accept "\n" ...
		formatter.printHelp(pw, width, appName, null, this.options, 2, 2, null, autoUsage);
		//formatter.printWrapped(pw, width, footer); // formatter didn't accept "\n" ...
		return header + sw.toString() + footer;
	}
}
