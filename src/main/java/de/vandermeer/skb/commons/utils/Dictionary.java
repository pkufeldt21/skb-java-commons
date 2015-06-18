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

import de.vandermeer.skb.base.Skb_Pair;
import de.vandermeer.skb.categories.IsDictionary;
import de.vandermeer.skb.collections.CollectionFilters;
import de.vandermeer.skb.commons.Predicates;

/**
 * A dictionary that translates values between two 'languages' based on pairs.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.4-SNAPSHOT build 150618 (18-Jun-15) for Java 1.8
 */
public class Dictionary<SOURCE, TARGET> implements IsDictionary<SOURCE, TARGET> {
	protected final List<Skb_Pair<SOURCE, TARGET>> translations;

	public Dictionary(){
		this.translations = new ArrayList<Skb_Pair<SOURCE, TARGET>>();
	}

	public Dictionary(Collection<Skb_Pair<SOURCE, TARGET>> coll){
		this.translations = new ArrayList<Skb_Pair<SOURCE, TARGET>>(coll);
	}

	public Dictionary(Skb_Pair<SOURCE, TARGET>[] ar){
		this.translations = new ArrayList<Skb_Pair<SOURCE, TARGET>>();
		if(ar!=null){
			for(Skb_Pair<SOURCE, TARGET> pair:ar){
				this.translations.add(pair);
			}
		}
	}

	@Override
	public final TARGET toTarget(SOURCE source){
		Collection<Skb_Pair<SOURCE, TARGET>> coll = this._getTargets(source);
		if(coll.size()>0){
			return coll.iterator().next().rhs();
		}
		return null;
	}

	public final void addTranslation(Skb_Pair<SOURCE, TARGET> add){
		if(add!=null){
			this.translations.add(add);
		}
	}

	public final Skb_Pair<SOURCE, TARGET> removeTranslation(int index){
		return this.translations.remove(index);
	}

	public final boolean removeTranslation(Skb_Pair<SOURCE, TARGET> remove){
		return this.translations.remove(remove);
	}

	@Override
	public final List<Skb_Pair<SOURCE, TARGET>> getTranslations(){
		return new ArrayList<Skb_Pair<SOURCE, TARGET>>(this.translations);
	}

	@Override
	public final List<TARGET> toAllTargets(SOURCE source){
		List<TARGET> ret = new ArrayList<TARGET>();
		if(source!=null){
			Collection<Skb_Pair<SOURCE, TARGET>> coll = this._getTargets(source);
			for(Skb_Pair<SOURCE, TARGET> pair : coll){
				ret.add(pair.rhs());
			}
		}
		return ret;
	}

	@Override
	public final List<SOURCE> toAllSources(TARGET target){
		List<SOURCE> ret = new ArrayList<SOURCE>();
		if(target!=null){
			Collection<Skb_Pair<SOURCE, TARGET>> coll = this._getSources(target);
			for(Skb_Pair<SOURCE, TARGET> pair : coll){
				ret.add(pair.lhs());
			}
		}
		return ret;
	}

	@Override
	public final SOURCE toSource(TARGET target){
		Collection<Skb_Pair<SOURCE, TARGET>> coll = this._getSources(target);
		if(coll.size()>0){
			return coll.iterator().next().lhs();
		}
		return null;
	}

	@Override
	public final Skb_Pair<SOURCE, TARGET> getPair4Target(TARGET target){
		Collection<Skb_Pair<SOURCE, TARGET>> coll = this._getSources(target);
		if(coll.size()>0){
			return coll.iterator().next();
		}
		return null;
	}

	@Override
	public final Skb_Pair<SOURCE, TARGET> getPair4Source(SOURCE source){
		Collection<Skb_Pair<SOURCE, TARGET>> coll = this._getTargets(source);
		if(coll.size()>0){
			return coll.iterator().next();
		}
		return null;
	}

	protected final Collection<Skb_Pair<SOURCE, TARGET>> _getTargets(SOURCE source){
		return new CollectionFilters<Skb_Pair<SOURCE, TARGET>>(){}.filter(Predicates.IS_TRANSLATION_FOR((TARGET)null, source), this.translations);
	}

	protected final Collection<Skb_Pair<SOURCE, TARGET>> _getSources(TARGET target){
		return new CollectionFilters<Skb_Pair<SOURCE, TARGET>>(){}.filter(Predicates.IS_TRANSLATION_FOR(target, (SOURCE)null), this.translations);
	}
}
