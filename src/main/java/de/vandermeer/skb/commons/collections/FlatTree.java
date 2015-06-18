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

import de.vandermeer.skb.base.utils.Skb_TextUtils;
import de.vandermeer.skb.categories.IsPath;
import de.vandermeer.skb.commons.Predicates;
import de.vandermeer.skb.composite.specialobject.NullObject;

/**
 * A classic implementation of the {@link Tree}.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.3 build 150618 (18-Jun-15) for Java 1.8
 */
public class FlatTree<E> implements Tree<E> {
	/** Map maintaining all tree elements */
	protected Map<String, E>sval;

	final boolean autoRoot=true;

	/** Creates a new classic tree. */
	public FlatTree(){
		this.sval = new HashMap<String, E>();
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
		this.sval.put(fqpn.toString(), value);
		return this.sval.containsKey(fqpn.toString());
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
//		return TreeUtils.getChildrenNames(TreeUtils.fqpnBuilderTree, TreeUtils.fqpnBuilderTree.join(fqpn), this.sval.keySet());
	}

	@Override
	public Collection<String> getChildrenNames(Object path, Object name) {
		return IsPath.GET_SUB_PATHS(Tree.defaulSeparator, Tree.treeJoiner.transform(new Pair<Object, Object>(path, name)), this.sval.keySet());
//		return TreeUtils.getChildrenNames(TreeUtils.fqpnBuilderTree, TreeUtils.fqpnBuilderTree.join(path, name), this.sval.keySet());
	}

	@Override
	public FlatTree<E> getCopy() {
		FlatTree<E> ret = new FlatTree<E>();
		ret.sval.putAll(this.sval);
		return ret;
	}

	@Override
	public FlatTree<E> getSubtree(Object fqpn) {
		return this.getSubtree(null, fqpn);
	}

	@Override
	public FlatTree<E> getSubtree(Object path, Object name) {
		FlatTree<E> ret = new FlatTree<E>();
		for(String key : IsPath.GET_SUB_PATHS(Tree.defaulSeparator, Tree.treeJoiner.transform(new Pair<Object, Object>(path, name)), this.sval.keySet())){
			ret.sval.put(key, this.sval.get(key));
//			ret.put(StringUtils.substringAfter(key, fqpn.toString()), map.get(key));
		}
		return ret;
	}

	@Override
	public E getValue(Object fqpn) {
		return this.sval.get(Tree.treeJoiner.transform(new Pair<Object, Object>(null, fqpn)).toString());
	}

	@Override
	public E getValue(Object path, Object name) {
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

	@Override
	public int size() {
		return this.sval.size();
	}

	@Override
	public String toString(){
		return Skb_TextUtils.MAP_TO_TEXT().transform(this.sval);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean merge(Tree<?> tree){
		boolean ret=false;
		try{
			Map<String, E> source = ((FlatTree<E>)tree).sval;
			for(String key : source.keySet()){
				if(!this.sval.containsKey(key)){
					this.sval.put(key, source.get(key));
				}
				else{
					E val = source.get(key);
					if(val!=null && !(val instanceof NullObject)){
						this.sval.put(key, val);
					}
				}
			}
			ret = true;
		}
		catch(Exception ignore){}
		return ret;
	}
}



///** Map maintaining all tree elements */
//protected TreeMap<String, Object>sval;
//
///** maximum allowed depth in the tree */
//protected int maxDepth;
//
///** the local FQPN builder, set to '/', autoRoot and autoClean */
//protected final Fqpn fqpnBuilder=new Fqpn('/', true, true, true);
//
///** explicit type for tree values, required when using NullObjects as value returns */
//protected Class<E> clazz;
//
///**
// * Creates a new common tree.
// * This tree cannot handle NullObjects, it will return <null> every time it encounters a NullObject as
// * value in the tree.
// */
//public CommonTree(){
//	this.sval=new TreeMap<String, Object>();
//	this.sval.put("/", NONode.get);
//	this.maxDepth=-1;
//}
//
///**
// * Creates a new tree with explicit typing.
// * This tree can handle NullObjects and will return a NullObject if the class is set to a super class
// * of the NullObjects (e.g. SkbType).
// * @param clazz explicit type for value testing
// */
//public CommonTree(Class<E> clazz){
//	this();
//	this.clazz=clazz;
//}
//
//@Override
//public boolean edge(Object fqpn, E value){
//	return this.edge(null, fqpn, value);
//}
//
//@SuppressWarnings("unchecked")
//@Override
//public boolean edge(Object path, Object name, E value){
//	String fqpn=this.buildFqpn(path, name);
//	if(fqpn!=null){
//		if(this.node(fqpn)==true){
//			Object edgeValue=this.sval.get(fqpn);
//			if(value!=null&&!(value instanceof NONone)){
//				if(edgeValue instanceof ComCollection){
//					((ComCollection<Object>)edgeValue).add(value);
//				}
//				else if(edgeValue instanceof NONode){
//					this.sval.put(fqpn, value);
//				}
//				else if(edgeValue instanceof NONull){
//					this.sval.put(fqpn, value);
//				}
//				else{
//					ComCollection<Object> ccol=new ComCollection<Object>();
//					ccol.add(edgeValue);
//					ccol.add(value);
//					this.sval.put(fqpn, ccol);
//				}
//			}
//			else if(value==null){
//				this.sval.put(fqpn, NONull.get);
//			}
//			else{
//				this.sval.put(fqpn, value);
//			}
//			return true;
//		}
//	}
//	return false;
//}
//
//@Override
//public E getValue(Object name){
//	return this.getValue(null, name);
//}
//
//@SuppressWarnings("unchecked")
//@Override
//public E getValue(Object path, Object name){
//	String fqpn=this.buildFqpn(path, name);
//	if(fqpn!=null&&this.sval.containsKey(fqpn)){
//		Object obj=this.sval.get(fqpn);
//		if(obj!=null){
//			if(this.clazz==null&&obj instanceof NullObject){	//means we can't return NOs!
//				return null;
//			}
//			else if(this.clazz!=null){							//means we can try to return NOs!
//				return CommonUtils.convert(obj, this.clazz, null, null, true);
//			}
//			else{												//means that obj is class of E, so cast is ok!
//				return CommonUtils.convert(obj, (Class<E>)obj.getClass(), null, null, true);
//			}
//		}
//	}
//	return null;
//}
//
//@Override
//public boolean node(Object node){
//	return this.node(this.buildFqpn(null, node));
//}
//
//@Override
//public boolean node(Object path, Object name){
//	return this.node(this.buildFqpn(path, name));
//}
//
//protected final boolean node(String fqpn){
//	List<String> fqpnArray=this.buildFqpnArray(fqpn);
//	if(fqpnArray!=null&&!this.sval.containsKey(fqpn)){
//		for(String elem : fqpnArray){
//			if(!this.sval.containsKey(elem)){
//				this.sval.put(elem, NONode.get);
//			}
//		}
//	}
//	return this.sval.containsKey(fqpn);
//}
//
//@Override
//public boolean removeValue(Object fqpn, E value){
//	return this.removeValue(null, fqpn, value);
//}
//
//@Override
//public boolean removeValue(Object path, Object name, E value){
//	String fqpn=this.buildFqpn(path, name);
//	return this.removeValue(fqpn, value);
//}
//
//protected final boolean removeValue(String fqpn, E value){
//	if(fqpn!=null&&this.sval.containsKey(fqpn)){
//		Object currentValue=this.sval.get(fqpn);
//		if(currentValue instanceof ComCollection){
//			@SuppressWarnings("unchecked")
//			ComCollection<Object> c=(ComCollection<Object>)currentValue;
//			if(c.contains(value)){
//				c.remove(value);
//				return true;
//			}
//		}
//		else if(!(currentValue instanceof NONode)){
//			this.sval.put(fqpn, NONode.get);
//			return true;
//		}
//	}
//	return false;
//}
