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

import java.util.Locale;


/**
 * An Internationalisation Manager.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.4-SNAPSHOT build 150618 (18-Jun-15) for Java 1.8
 */
public interface I18NManager {
	/**
	 * Translate the given string using the currently set domain.
	 * @param translate input string to translate
	 * @return translation or an empty string ""
	 */
	String _t(String translate);

	/**
	 * Return translation for the given string from the given domain (package name).
	 * @param domain domain to use
	 * @param translate input string to translate
	 * @return translation or an empty string ""
	 */
	String _t(String domain, String translate);

	/**
	 * Add a domain described by a package name and locale information.
	 * @param pkg name of the package (later used as key/domain ID)
	 * @param locale locale information to be used to load a resource bundle
	 */
	void addDomain(String pkg, Locale locale);

	/**
	 * Set the domain to be used for getting translations.
	 * @param pkg package name (domain ID)
	 */
	void setTextDomain(String pkg);
}
