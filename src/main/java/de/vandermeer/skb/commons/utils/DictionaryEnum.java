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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import de.vandermeer.skb.base.Skb_Pair;
import de.vandermeer.skb.categories.IsDictionary;
import de.vandermeer.skb.collections.CollectionFilters;
import de.vandermeer.skb.commons.Predicates;

/**
 * A dictionary that uses enumerates as translation pairs.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.4-SNAPSHOT build 150618 (18-Jun-15) for Java 1.8
 */
public class DictionaryEnum<SOURCE, TARGET, E extends Enum<E> & Skb_Pair<SOURCE, TARGET>> implements IsDictionary<SOURCE, TARGET> {

	/** List of translations as pairs of declared types. */
	protected final List<E> translations;

	/**
	 * Returns a new dictionary w/o any translations.
	 */
	public DictionaryEnum(){
		this.translations = new ArrayList<E>();
	}

	/**
	 * Returns a new dictionary initialised with given translations.
	 * @param coll collection of translations
	 */
	public DictionaryEnum(Collection<E> coll){
		this.translations = new ArrayList<E>(coll);
	}

	/**
	 * Returns a new dictionary initialised with given translations.
	 * @param ar array of translations
	 */
	public DictionaryEnum(E[] ar){
		this.translations = new ArrayList<E>();
		if(ar!=null){
			for(E pair : ar){
				this.translations.add(pair);
			}
		}
	}

	@Override
	public final TARGET toTarget(SOURCE source){
		Collection<E> coll = this._getTargets(source);
		if(coll.size()>0){
			return coll.iterator().next().rhs();
		}
		return null;
	}

	@Override
	public final List<Skb_Pair<SOURCE, TARGET>> getTranslations(){
		return new ArrayList<Skb_Pair<SOURCE, TARGET>>(this.translations);
	}

	@Override
	public final SOURCE toSource(TARGET target){
		Collection<E> coll = this._getSources(target);
		if(coll.size()>0){
			return coll.iterator().next().lhs();
		}
		return null;
	}

	@Override
	public final E getPair4Target(TARGET target){
		Collection<E> coll = this._getSources(target);
		if(coll.size()>0){
			return coll.iterator().next();
		}
		return null;
	}

	@Override
	public final E getPair4Source(SOURCE source){
		Collection<E> coll = this._getTargets(source);
		if(coll.size()>0){
			return coll.iterator().next();
		}
		return null;
	}

	@Override
	public final List<TARGET> toAllTargets(SOURCE source){
		List<TARGET> ret = new ArrayList<TARGET>();
		if(source!=null){
			Collection<E> coll = this._getTargets(source);
			for(E pair : coll){
				ret.add(pair.rhs());
			}
		}
		return ret;
	}

	@Override
	public final List<SOURCE> toAllSources(TARGET target){
		List<SOURCE> ret = new ArrayList<SOURCE>();
		if(target!=null){
			Collection<E> coll = this._getSources(target);
			for(E pair : coll){
				ret.add(pair.lhs());
			}
		}
		return ret;
	}

	/**
	 * Returns a list of all translation objects for a given source.
	 * @param source translation source to filter for
	 * @return list of all translations for the given source
	 */
	protected final Collection<E> _getTargets(SOURCE source){
		Predicate<E> predicate = Predicates.IS_TRANSLATION_FOR((TARGET)null, source);
		return new CollectionFilters<E>(){}.filter(predicate, this.translations);
	}

	/**
	 * Returns a list of all translation objects for a given target.
	 * @param target translation source to filter for
	 * @return list of all translations for the given source
	 */
	protected final Collection<E> _getSources(TARGET target){
		Predicate<E> predicate = Predicates.IS_TRANSLATION_FOR(target, (SOURCE)null);
		return new CollectionFilters<E>(){}.filter(predicate, this.translations);
	}
}
