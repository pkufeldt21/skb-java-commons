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

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Implementation of the {@link I18NManager} interface using Resource Bundles.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.3-SNAPSHOT build 141210 (10-Dec-14) for Java 1.8
 */
public class I18NManagerRB implements I18NManager {
	/** List of keys (domains) with associated resource bundle */
	LinkedHashMap <String, ResourceBundle> entries;

	/** Current domain for resources */
	private String currentDomain;

	public I18NManagerRB(){
		this.entries = new LinkedHashMap <String, ResourceBundle>();
		currentDomain = new String();
	}

	@Override
	public String _t(String translate) {
		if(this.currentDomain!=null){
			return this.entries.get(this.currentDomain).getString(translate);
		}
		return "";
	}

	@Override
	public String _t(String domain, String translate) {
		if(this.entries.containsKey(domain)){
			return this.entries.get(domain).getString(translate);
		}
		return "";
	}

	@Override
	public void addDomain(String pkg, Locale locale) {
		ResourceBundle rb;
		try{
			if(locale==null){
				rb = PropertyResourceBundle.getBundle(pkg);
			}
			else{
				rb = PropertyResourceBundle.getBundle(pkg, locale);
			}
			this.entries.put(pkg, rb);

		}
		catch (Exception e) {
			//TODO
			//logger.error("catched exception: "+e);
		}
	}

	@Override
	public void setTextDomain(String pkg) {
		if(pkg!=null){
			this.currentDomain = pkg;
		}
	}

	@Override
	public String toString() {
		return "I18N Manager";
	}
}
