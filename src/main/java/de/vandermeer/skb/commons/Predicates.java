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

import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;

import org.apache.commons.lang3.text.StrBuilder;

import de.vandermeer.skb.base.Skb_Pair;

/**
 * Collection of useful predicates.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.3 build 150618 (18-Jun-15) for Java 1.8
 */
public abstract class Predicates {

	/**
	 * Returns a predicate that returns true if a given pair is a translation for a specified target or source.
	 * If target and source are null, the predicate will return false. If the target is not null, the predicate will
	 * return true if the pair is a translation for the target. If the source is not null, the predicate will return
	 * true if the pair is a translation for the source.
	 * @param <SOURCE> source (left) of pair
	 * @param <TARGET> target (right) of pair
	 * @param <P> a pair of source and target
	 * @param target the target to evaluate, null if the source should be used
	 * @param source the source to evaluate, null if the target should be used
	 * @return the predicate
	 */
	public final static <SOURCE, TARGET, P extends Skb_Pair<SOURCE, TARGET>> Predicate<P> IS_TRANSLATION_FOR(final TARGET target, final SOURCE source){
		return new Predicate<P>(){
			@Override public boolean test(P pair){
				if(target==null && source==null){
					return false;
				}
				if(target!=null && target.equals(pair.rhs())){
					return true;
				}
				if(source!=null && source.equals(pair.lhs())){
					return true;
				}
				return false;
			}
		};
	}



//	public final static <T> Predicate<T> CONTAINS(final Collection<T> coll){
//		return new Predicate<T>(){
//			@Override public boolean evaluate(T t){return (coll==null)?false:coll.contains(t);}
//		};
//	}

//	public final static Predicate<String> CONTAINS_ENUM(final Enum<?>[] coll){
//		return new Predicate<String>(){
//			@Override public boolean evaluate(String t){
//				if(coll==null){
//					return false;
//				}
//				else{
//					for(Enum<?> e:coll){
//						if(e.name().equals(t)){
//							return true;
//						}
//					}
//					return false;
//				}
//			}
//		};
//	}

//	public final static <T> Predicate<T> CONTAINS_NOT(final Collection<T> coll){
//		return new Predicate<T>(){
//			@Override public boolean evaluate(T t){return (coll==null)?true:!(coll.contains(t));}
//		};
//	}

//	public final static <T> Predicate<T> IS_SAME(final T b){
//		return new Predicate<T>(){
//			@Override public boolean evaluate(T a){
//				if(a==null&&b==null){
//					return true;
//				}
//				if(a==null){
//					return false;
//				}
//				return a.equals(b);
//			}
//		};
//	}


	/**
	 * Returns a predicate that evaluates to true if the set contains strings starting with a given character sequence.
	 * @param nodes set of strings as base
	 * @return predicate that returns true if set contains strings starting with it, false otherwise
	 */
	final public static Predicate<StrBuilder> CONTAINS_STRINGS_STARTING_WITH(final Set<String> nodes){
		return new Predicate<StrBuilder>(){
			@Override public boolean test(final StrBuilder fqpn){
				if(fqpn==null){
					return false;
				}
				String key = fqpn.toString();
				if(!nodes.contains(key)){
					return false;
				}

				Set<String> tail = new TreeSet<String>(nodes).tailSet(key, false);
				if(tail.size()==0){
					return false;
				}

				String child = tail.iterator().next();
				if(child.startsWith(key)){
					return true;
				}
				return false;
			}
		};
	}
}
