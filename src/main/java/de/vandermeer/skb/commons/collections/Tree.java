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
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.misc.Pair;
import org.apache.commons.lang3.text.StrBuilder;

import de.vandermeer.skb.base.Skb_Transformer;
import de.vandermeer.skb.categories.IsPath;
import de.vandermeer.skb.composite.CompositeObject;

/**
 * A Tree.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.4-SNAPSHOT build 150618 (18-Jun-15) for Java 1.8
 */
public interface Tree<E> extends CompositeObject {

	/** The default separator for tree paths set to "/" */
	public static String defaulSeparator="/";

	public static final Skb_Transformer<Pair<Object, Object>, StrBuilder> treeJoiner = IsPath.JOIN_PATH_ELEMENTS(Table.defaulSeparator, true, true);

	/**
	 * Clears the tree, removes all values except root.
	 */
	void clear();

	/**
	 * Tests if the tree contains a path.
	 * @param fqpn path to be tested
	 * @return true if the path exists, false otherwise
	 */
	boolean containsNode(Object fqpn);

	/**
	 * Tests if the tree contains a path.
	 * @param path path for FQPN
	 * @param name name for FQPN
	 * @return true if the path exists, false otherwise
	 */
	boolean containsNode(Object path, Object name);

	/**
	 * Returns the children of a node.
	 * @param fqpn name of the node
	 * @return name of all children, empty of none found
	 */
	Collection<String> getChildrenNames(Object fqpn);

	/**
	 * Returns the children of a node.
	 * @param path path for the node
	 * @param name name of the node
	 * @return name of all children, empty of none found
	 */
	Collection<String> getChildrenNames(Object path, Object name);

	/**
	 * Returns a complete subtree with the given path as root node.
	 * @param fqpn root node
	 * @return complete subtree, empty if fqpn has no children
	 */
	Tree<E> getSubtree(Object fqpn);

	/**
	 * Returns a complete subtree with the given path as root node.
	 * @param path path of the root node
	 * @param name name of the root node
	 * @return complete subtree, empty if node has no children
	 */
	Tree<E> getSubtree(Object path, Object name);

	/**
	 * Returns the value of a node.
	 * @param fqpn node
	 * @return value, null can mean that the node does not exist or that the value of the node is null
	 */
	E getValue(Object fqpn);

	/**
	 * Returns the value of a node.
	 * @param path path for the node
	 * @param name name of the node
	 * @return value, null can mean that the node does not exist or that the value of the node is null
	 */
	E getValue(Object path, Object name);

	/**
	 * Tests if the given path has children
	 * @param fqpn path to test
	 * @return true if the path has children, false otherwise
	 */
	boolean hasChildren(Object fqpn);

	/**
	 * Tests if the given path has children
	 * @param path path of the test node
	 * @param name name of the test node
	 * @return true if the path has children, false otherwise
	 */
	boolean hasChildren(Object path, Object name);

	/**
	 * Tests if the tree is empty.
	 * @return true if tree is empty, false otherwise
	 */
	boolean isEmpty();

	/**
	 * Returns a set view of all keys in the tree.
	 * @return set of current keys, not necessarily in any order
	 */
	Set<String> keys();

	/**
	 * Adds a new empty node in the tree.
	 * @param fqpn name of the node
	 * @return true if created, false otherwise
	 * @see #addNode(Object, Object)
	 */
	boolean addNode(Object fqpn);

	/**
	 * Adds a new empty node in the tree.
	 * @param path oath element for the node
	 * @param name name element of the node
	 * @return true if node is in tree, false otherwise
	 */
	boolean addNode(Object path, Object name);

	/**
	 * Adds a node with a value.
	 * @param fqpn path for the value
	 * @param value value to be processed
	 * @return true if the value was added/put, false otherwise
	 * @see #addNodeWithValue(Object, Object, Object)
	 */
	boolean addNodeWithValue(Object fqpn, E value);

	/**
	 * Adds a node with a value.
	 * The value will not be added/put if path and name do not compute into a valid FQPN or an error occurred
	 * while creating the required FQPN in the tree.
	 * @param path path for the FQPN
	 * @param name name for the FQPN
	 * @param value value to be processed
	 * @return true if the value was added/put, false otherwise
	 */
	boolean addNodeWithValue(Object path, Object name, E value);

	/**
	 * Removes a node from the tree.
	 * @param fqpn name of the node to be removed
	 * @return true if the node does not exist anymore, false otherwise
	 * @see #removeNode(Object, Object)
	 */
	boolean removeNode(Object fqpn);

	/**
	 * Removes a node from the tree.
	 * The node, its values and all its children will be removed.
	 * The root node cannot be removed.
	 * @param path path for the node
	 * @param name name for the node
	 * @return true if the node does not exist anymore, false otherwise
	 */
	boolean removeNode(Object path, Object name);

	/**
	 * Removes the value for the node.
	 * @param fqpn path for the node
	 * @return true if value was removed, false otherwise
	 */
	boolean removeValue(Object fqpn);

	/**
	 * Removes the value for the node.
	 * @param path path for the node
	 * @param name name of the node
	 * @return true if value was removed, false otherwise
	 */
	boolean removeValue(Object path, Object name);

	/**
	 * Returns the current size of the tree.
	 * @return size of the tree
	 */
	int size();

	/**
	 * Merges two trees. The result of this method depends on the tree implementation used for both trees.
	 * @param tree source for merging
	 * @return true if merge was successful, false otherwise
	 */
	boolean merge(Tree<?> tree);

	/**
	 * Adds the node to the map with null value.
	 * @param fqpn name of the node to be added
	 * @param map map to add the node to
	 * @param autoRoot automatically add a root separator
	 * @return true on success, false otherwise
	 */
	static boolean addNodeWithNull(StrBuilder fqpn, Map<String, ?> map, Boolean autoRoot){
		if(fqpn==null||map==null){
			return false;
		}

		Skb_Transformer<StrBuilder, List<String>> tr=IsPath.PATH_TO_ARRAY_OF_PATHS("/", autoRoot);
		List<String> fqpnArray=tr.transform(fqpn);
		for(String elem : fqpnArray){
			if(!map.containsKey(elem)){
				map.put(elem, null);
			}
		}
		return map.containsKey(fqpnArray.get(fqpnArray.size()-1));
	}

	/**
	 * Removes a node and all its children from the map
	 * @param fqpn node to be removed
	 * @param map map from which the node should be removed
	 * @param separator path separator
	 * @return true if successful, false otherwise
	 */
	public static boolean removeNode(StrBuilder fqpn, Map<String, ?> map, String separator){
		if(fqpn==null||map==null||fqpn==null){
			return false;
		}

		for(String s:IsPath.GET_SUB_PATHS(separator, fqpn, map.keySet())){
			map.remove(s);
		}
		map.remove(fqpn.toString());
		return !map.containsKey(fqpn.toString());
	}
}
