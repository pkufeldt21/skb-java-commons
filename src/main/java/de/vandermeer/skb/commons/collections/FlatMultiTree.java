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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.misc.Pair;
import org.apache.commons.lang3.text.StrBuilder;

import de.vandermeer.skb.base.categories.IsPath;
import de.vandermeer.skb.base.composite.coin.NullObject;
import de.vandermeer.skb.base.utils.collections.Skb_CollectionTransformer;
import de.vandermeer.skb.commons.Predicates;

/**
 * A multi-value implementation of the {@link Tree}.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.4 build 150701 (01-Jul-15) for Java 1.8
 */
public class FlatMultiTree<E> implements Tree<E> {
	/** Map maintaining all tree elements */
	protected Map<String, ComCollection<E>>sval;

	final boolean autoRoot=true;

	/** Creates a new multi tree */
	public FlatMultiTree(){
		this.sval = new HashMap<String, ComCollection<E>>();
	}

	@Override
	public void clear() {
		this.sval.clear();
	}

	@Override
	public boolean containsNode(Object fqpn) {
		return this.sval.containsKey(Tree.treeJoiner.transform(new Pair<Object, Object>(null, fqpn)).toString());
	}

	@Override
	public boolean containsNode(Object path, Object name) {
		return this.sval.containsKey(Tree.treeJoiner.transform(new Pair<Object, Object>(path, name)).toString());
	}

	@Override
	public Collection<String> getChildrenNames(Object fqpn) {
		return IsPath.GET_SUB_PATHS(Tree.defaulSeparator, Tree.treeJoiner.transform(new Pair<Object, Object>(null, fqpn)), this.sval.keySet());
	}

	@Override
	public Collection<String> getChildrenNames(Object path, Object name) {
		return IsPath.GET_SUB_PATHS(Tree.defaulSeparator, Tree.treeJoiner.transform(new Pair<Object, Object>(path, name)), this.sval.keySet());
	}

	@Override
	public FlatMultiTree<E> getCopy() {
		FlatMultiTree<E> ret = new FlatMultiTree<E>();
		ret.sval.putAll(this.sval);
		return ret;
	}

	@Override
	public FlatMultiTree<E> getSubtree(Object fqpn) {
		return this.getSubtree(null, fqpn);
	}

	@Override
	public FlatMultiTree<E> getSubtree(Object path, Object name) {
		FlatMultiTree<E> ret = new FlatMultiTree<E>();
		for(String key : IsPath.GET_SUB_PATHS(Tree.defaulSeparator, Tree.treeJoiner.transform(new Pair<Object, Object>(path, name)), this.sval.keySet())){
			ret.sval.put(key, this.sval.get(key));
		}
		return ret;
	}

	@Override
	public E getValue(Object fqpn) {
		return this.getValue(null, fqpn);
	}

	@Override
	public E getValue(Object path, Object name) {
		ComCollection<E> coll = this.sval.get(Tree.treeJoiner.transform(new Pair<Object, Object>(path, name)).toString());
		if(coll!=null){
			return coll.getFirst();
		}
		return null;
	}

	/**
	 * Returns the values associated with the FQPN.
	 * @param fqpn path name to look for
	 * @return collection of values associated with the path name
	 */
	public ComCollection<E> getValueMulti(Object fqpn){
		return this.getValueMulti(null, fqpn);
	}

	/**
	 * Returns the values associated with the FQPN given by path and name.
	 * @param path path element of the FQPN
	 * @param name name element of the FQPN
	 * @return collection of values associated with the path and name
	 */
	public ComCollection<E> getValueMulti(Object path, Object name){
		return this.sval.get(Tree.treeJoiner.transform(new Pair<Object, Object>(path, name)).toString());
	}

	@Override
	public boolean hasChildren(Object fqpn) {
		return Predicates.CONTAINS_STRINGS_STARTING_WITH(this.sval.keySet()).test(Tree.treeJoiner.transform(new Pair<Object, Object>(null, fqpn)));
	}

	@Override
	public boolean hasChildren(Object path, Object name) {
		return Predicates.CONTAINS_STRINGS_STARTING_WITH(this.sval.keySet()).test(Tree.treeJoiner.transform(new Pair<Object, Object>(path, name)));
	}

	@Override
	public boolean isEmpty() {
		return this.sval.isEmpty();
	}

	@Override
	public Set<String> keys() {
		return this.sval.keySet();
//		return new TreeSet<String>(this.sval.keySet());
	}

	@Override
	public boolean addNode(Object fqpn) {
		return Tree.addNodeWithNull(Tree.treeJoiner.transform(new Pair<Object, Object>(null, fqpn)), this.sval, this.autoRoot);
	}

	@Override
	public boolean addNode(Object path, Object name) {
		return Tree.addNodeWithNull(Tree.treeJoiner.transform(new Pair<Object, Object>(path, name)), this.sval, this.autoRoot);
	}

	@Override
	public boolean addNodeWithValue(Object fqpn, E value) {
		return this.addNodeWithValue(null, fqpn, value);
	}

	@Override
	public boolean addNodeWithValue(Object path, Object name, E value) {
		StrBuilder fqpn = Tree.treeJoiner.transform(new Pair<Object, Object>(path, name));
		Tree.addNodeWithNull(Tree.treeJoiner.transform(new Pair<Object, Object>(path, name)), this.sval, this.autoRoot);
		String key = fqpn.toString();
		if(this.sval.containsKey(key)){
			if(this.sval.get(key)!=null){
				this.sval.get(key).add(value);
			}
			else{
				ComCollection<E> add = new ComCollection<E>();
				add.add(value);
				this.sval.put(fqpn.toString(), add);
			}
		}
		return this.sval.containsKey(fqpn.toString());
	}

	@Override
	public boolean removeNode(Object fqpn) {
		return Tree.removeNode(Tree.treeJoiner.transform(new Pair<Object, Object>(null, fqpn)), this.sval, Tree.defaulSeparator);
	}

	@Override
	public boolean removeNode(Object path, Object name) {
		return Tree.removeNode(Tree.treeJoiner.transform(new Pair<Object, Object>(path, name)), this.sval, Tree.defaulSeparator);
	}

	@Override
	public boolean removeValue(Object fqpn) {
		return this.removeValue(null, fqpn);
	}

	@Override
	public boolean removeValue(Object path, Object name) {
		String key = Tree.treeJoiner.transform(new Pair<Object, Object>(path, name)).toString();
		if(this.sval.containsKey(key)){
			this.sval.put(key, null);
			return true;
		}
		return false;
	}

	/**
	 * Removes a specific value from a node.
	 * @param fqpn qualified name of the node
	 * @param value value to be removed
	 * @return true if removed, false otherwise (e.g. fqpn did not exist, value did not exist)
	 */
	public boolean removeMultiValue(Object fqpn, E value) {
		return this.removeMultiValue(null, fqpn, value);
	}

	/**
	 * Removes a specific value from a node.
	 * @param path path to the node
	 * @param name name of the node
	 * @param value value to be removed
	 * @return true if removed, false otherwise (e.g. fqpn did not exist, value did not exist)
	 */
	public boolean removeMultiValue(Object path, Object name, E value) {
		String key = Tree.treeJoiner.transform(new Pair<Object, Object>(path, name)).toString();
		ComCollection<E> coll=this.sval.get(key);
		if(coll!=null){
			coll.remove(value);
			return true;
		}
		return false;
	}

	@Override
	public int size() {
		return this.sval.size();
	}

	@Override
	public String toString(){
		return Skb_CollectionTransformer.MAP_TO_TEXT(this.sval);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean merge(Tree<?> tree){
		boolean ret = false;
		try{
			Map<String, ComCollection<E>>source=((FlatMultiTree<E>)tree).sval;
			for(String key : source.keySet()){
				if(!this.sval.containsKey(key)){
					this.sval.put(key, source.get(key));
				}
				else{
					for(E col : source.get(key)){
						if(col!=null && !(col instanceof NullObject)){
							this.sval.get(key).add(col);
						}
					}
				}
			}
			ret=true;
		}
		catch(Exception ignore){}
		return ret;
	}
}
