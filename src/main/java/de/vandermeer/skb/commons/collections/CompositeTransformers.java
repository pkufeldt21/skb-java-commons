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

package de.vandermeer.skb.commons.collections;

import de.vandermeer.skb.base.Skb_Transformer;
import de.vandermeer.skb.composite.CompositeObject;
import de.vandermeer.skb.composite.specialobject.NONone;
import de.vandermeer.skb.composite.specialobject.NONull;

/**
 * Transformations for composite classes.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.2 build 140626 (26-Jun-14) with Java 1.8
 */
public abstract class CompositeTransformers {

	/**
	 * Returns a transformer that can 'explode' an object, that is returning a tree or collection, depending on the contents and format of the string representation of the object.
	 * 
	 * The String explodes to a tree if it has the following notation: <code>key1%val1,key2%val2%key3,val3,...</code>
	 * The String explodes to a collection if it has the following notation: <code>string1,string2,string3,...</code>
	 * All strings that are added to a collection or put in a tree are trimmed.
	 * @return	Transformer that explodes an object. The transformer returns {@link CompositeObject}
	 * 			which is either a tree (if input explodes to an associated array, must contain "%") or
	 * 			an collection (if input explodes to a simple String[], must contain "',"); or
	 * 			{@link NONull} if the input was null; or
	 * 			null if the length of toString on the value was 0;
	 * 			 {@link NONone} otherwise
	 */
	public static final Skb_Transformer<Object, CompositeObject> EXPLODE_OBJECT(){
		return new Skb_Transformer<Object, CompositeObject>(){
			@Override public CompositeObject transform(Object src){
				if(src==null){
					return NONull.get;
				}
				String source = src.toString();

				String ts[];
				if(source.length()==0){
					return null;
				}
				else if (source.contains("%")) {
					FlatTree<String> ret = new FlatTree<String>();
					String[] comma = source.split(",");
					for (int i=0; i<comma.length; i++) {
						comma[i] = comma[i].trim();
						ts = comma[i].split("%");
						if (ts.length==2){
							ret.addNodeWithValue((String)ts[0].trim(), new String(ts[1].trim()));
						}
					}
					return ret;
				}
				else if (source.contains(",")) {
					ts = source.split(",");
					ComCollection<String> ret=new ComCollection<String>();
					for(int i=0; i<ts.length; i++){
						ret.add(ts[i].trim());
					}
					return ret;
				}
				else{
					return NONone.get;
				}
			}
		};
	}
}
