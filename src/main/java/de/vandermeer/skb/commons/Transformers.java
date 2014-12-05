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
package de.vandermeer.skb.commons;

import de.vandermeer.skb.base.Skb_Renderable;
import de.vandermeer.skb.base.Skb_Transformer;
import de.vandermeer.skb.base.utils.Skb_Antlr4Utils;
import de.vandermeer.skb.base.utils.Skb_TextUtils;
import de.vandermeer.skb.categories.CategoryWithValue;

/**
 * Collection of useful transformers.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.2 build 140626 (26-Jun-14) with Java 1.8
 */
public abstract class Transformers {

	/**
	 * Chained transformer for an object source and a string target.
	 * Uses ANTRL4, IsKey, IsPath, Renderable and TextUtils transformers.
	 * @return text representation of the given object, null if no transformer was successful
	 */
	@SuppressWarnings("unchecked")
	public static final Skb_Transformer<Object, String> OBJECT_TO_TEXT(){
		return Skb_Transformer.CHAIN(Skb_Antlr4Utils.ANTLR_TO_TEXT(), CategoryWithValue.CAT_TO_VALUESTRING(), Skb_Renderable.OBJECT_TO_RENDERABLE_VALUE(), Skb_TextUtils.TO_STRING());
	}
}
