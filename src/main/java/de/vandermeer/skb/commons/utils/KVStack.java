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

import java.util.Stack;

/**
 *  A Stack of key/value pairs.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.3-SNAPSHOT build 141210 (10-Dec-14) for Java 1.8
 */
public class KVStack {
	/** Local value */
	protected Stack<String> sval;

	/** Local separator for key/values */
	String separator;

	public KVStack() {
		this.sval = new Stack<String>();
		this.separator = "@";
	}

//	/**
//	 * Returns the scope in form of a stack of strings.
//	 * @return scope as stack
//	 */
//	public Stack<String> asList() {
//		return this.sval;
//	}

	/**
	 * Returns a deep copy of the stack.
	 * @return deep copy
	 */
	public KVStack getCopy() {
		KVStack ret = new KVStack();
		ret.sval = new Stack<String>();
		sval.addAll(this.sval);
		return ret;
	}

	/**
	 * Remove the last element from the scope.
	 */
	public void pop() {
		this.sval.pop();
	}

	/**
	 * Add a new element to the end of the scope.
	 * The two input parameters are combined into a single string (separated by a fixed separator).
	 * @param key part of the new element
	 * @param value value part of the new element
	 * @return true if new element is not in stack (and pushed), false otherwise (no push)
	 */
	public boolean push(String key, String value) {
		if(this.sval.contains(key + this.separator+value)){
			return false;
		}
		this.sval.push(key + this.separator+value);
		return true;
	}

	/**
	 * clears the scope removing all contents.
	 */
	public void reset() {
		this.sval.clear();
	}

	@Override
	public String toString() {
		return this.sval.toString();
	}
}