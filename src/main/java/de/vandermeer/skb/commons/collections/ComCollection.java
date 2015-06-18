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

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.vandermeer.skb.collections.CollectionTools;
import de.vandermeer.skb.collections.IsCollectionStrategy;
import de.vandermeer.skb.collections.IsSortedSetStrategy;
import de.vandermeer.skb.collections.ListStrategy;
import de.vandermeer.skb.composite.CompositeObject;

/**
 * An SKB specific Collection of Objects.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.4-SNAPSHOT build 150618 (18-Jun-15) for Java 1.8
 */
public class ComCollection<E> implements CompositeObject, Collection<E> {

	/** Local collection */
	protected Collection<E> sval;

	/** Strategy used for the local collection */
	protected IsCollectionStrategy strategy;

	/** Alternative sorted strategy for local collection */
	protected IsSortedSetStrategy strategySorted;

	/** Comparator for a sorted strategy */
	Comparator<E> comparator;

	/** Creates a new collection with default strategy ({@link ListStrategy#ARRAY_LIST}). */
	public ComCollection(){
		this._init(null, null, null, null);
	}

	/**
	 * Creates a new collection with the given strategy.
	 * @param strategy strategy to be used for the collection, {@link ListStrategy#ARRAY_LIST} if null
	 */
	public ComCollection(IsCollectionStrategy strategy){
		this._init(strategy, null, null, null);
	}

	/**
	 * Creates a new collection with given strategy and filled with input collection.
	 * @param strategy strategy to be used for the collection, {@link ListStrategy#ARRAY_LIST} if null
	 * @param collection initial values of the collection
	 */
	public ComCollection(IsCollectionStrategy strategy, Collection<E> collection){
		this._init(strategy, null, collection, null);
	}

	/**
	 * Creates a new collection with the given sorted strategy and comparator.
	 * @param sortedStrategy strategy to be used for the sorted set, {@link ListStrategy#ARRAY_LIST} if null
	 * @param comparator comparator to be used for the sorted set, if null the strategy will be set to {@link ListStrategy#ARRAY_LIST}
	 */
	public ComCollection(IsSortedSetStrategy sortedStrategy, Comparator<E> comparator){
		this._init(null, sortedStrategy, null, comparator);
	}

	/**
	 * Creates a new collection with the given sorted strategy and comparator and filled with input collection.
	 * @param sortedStrategy strategy to be used for the sorted set, {@link ListStrategy#ARRAY_LIST} if null
	 * @param comparator comparator to be used for the sorted set, if null the strategy will be set to {@link ListStrategy#ARRAY_LIST}
	 * @param collection initial values of the collection
	 */
	public ComCollection(IsSortedSetStrategy sortedStrategy, Comparator<E> comparator, Collection<E> collection){
		this._init(null, sortedStrategy, collection, comparator);
	}

	/**
	 * Initialises the ComCollection.
	 * Strategies can be sorted or none-sorted strategies. Default is an array list. The comparator is required for sorted strategies. The collection
	 * is used for initially filling ComCollection.
	 * @param strategy collection strategy, defaults to array list
	 * @param sortedStrategy sorted collection strategy, defaults to array list (which is of course not sorted)
	 * @param collection values to add to the ComCollection
	 * @param comparator method to sort values in a sorted collection strategy
	 */
	protected final void _init(IsCollectionStrategy strategy, IsSortedSetStrategy sortedStrategy, Collection<E> collection, Comparator<E> comparator){
		if(strategy==null && sortedStrategy==null){
			this.strategy = ListStrategy.ARRAY_LIST;
		}
		else{
			this.strategy = strategy;
			this.strategySorted = sortedStrategy;
			this.comparator = comparator;
		}

		if(this.strategy!=null){
			this.sval = this.strategy.get(collection);
		}
		else if(this.strategySorted!=null && this.comparator==null){
			this.strategySorted = null;
			this.strategy = ListStrategy.ARRAY_LIST;
			this.sval = this.strategy.get(collection);
		}
		else{
			this.sval = this.strategySorted.get(collection, comparator);
		}
	}

	@Override
	public boolean add(E obj) {
		return this.sval.add(obj);
	}

	@Override
	public boolean addAll(Collection<? extends E> collection) {
		return this.sval.addAll(collection);
	}

	@Override
	public void clear() {
		this.sval.clear();
	}

	@Override
	public boolean contains(Object obj) {
		return this.sval.contains(obj);
	}

	@Override
	public boolean containsAll(Collection<?> collection) {
		return this.sval.contains(collection);
	}

	/**
	 * Returns the first value of the collection.
	 * @return first value if there is one, null otherwise
	 */
	public E getFirst(){
		if(this.sval==null || this.sval.size()==0){
			return null;
		}
		if(this.sval instanceof List){
			return ((List<E>)this.sval).get(0);
		}
		if(this.sval instanceof Set){
			for(E elem : (Set<E>)this.sval){
				return elem;
			}
		}
		return null;
	}

	@Override
	public boolean isEmpty() {
		return this.sval.isEmpty();
	}

	@Override
	public Iterator<E> iterator() {
		return this.sval.iterator();
	}

	@Override
	public boolean remove(Object obj) {
		return this.sval.remove(obj);
	}

	@Override
	public boolean removeAll(Collection<?> collection) {
		return this.sval.removeAll(collection);
	}

	@Override
	public boolean retainAll(Collection<?> collection) {
		return this.sval.retainAll(collection);
	}

	@Override
	public int size() {
		return this.sval.size();
	}

	@Override
	public ComCollection<E> getCopy() {
		return new ComCollection<E>(this.strategy, this.sval);
	}

	@Override
	public Object[] toArray() {
		return this.sval.toArray();
	}

	@Override
	public <T> T[] toArray(T[] array) {
		return this.sval.toArray(array);
	}

	@Override
	public String toString() {
		return CollectionTools.COLLECTION_TO_TEXT(this.sval);
	}
}
