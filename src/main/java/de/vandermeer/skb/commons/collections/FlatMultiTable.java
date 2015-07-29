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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.misc.Pair;

import de.vandermeer.skb.base.categories.IsPath;
import de.vandermeer.skb.base.categories.kvt.IsAttributeKey;
import de.vandermeer.skb.base.utils.collections.IsSetStrategy;
import de.vandermeer.skb.base.utils.collections.SetStrategy;
import de.vandermeer.skb.base.utils.collections.Skb_CollectionTransformer;
import de.vandermeer.skb.configuration.EAttributeKeys;

/**
 * A multi-value implementation of the {@link Table}.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.4 build 150701 (01-Jul-15) for Java 1.8
 */
public class FlatMultiTable<E> implements Table<E> {
	/** Map maintaining all tree elements */
	protected Map<String, ComCollection<E>>sval;

	/** table columns */
	protected Set<IsAttributeKey> columns;

	/** strategy for the table columns */
	protected IsSetStrategy strategy;

	final boolean autoClean = true;

	final boolean autoRoot = true;

	final boolean autoRootArray = false;

	public FlatMultiTable(){
		this.init(null, null);
	}

	/**
	 * Creates a new table.
	 * @param strategy strategy for the table
	 * @param columns of columns for each row
	 */
	public FlatMultiTable(IsSetStrategy strategy, Collection<IsAttributeKey> columns){
		this.init(strategy, columns);
	}

	/**
	 * Creates a new table.
	 * @param strategy strategy for the table
	 * @param columns of columns for each row
	 */
	public FlatMultiTable(IsSetStrategy strategy, IsAttributeKey[] columns){
		if(columns!=null){
			this.init(strategy, Arrays.asList(columns));
		}
		else{
			this.init(strategy, null);
		}
	}

	@Override
	public boolean addRow(Object row) {
		return Table.addRowWithNull(row, this.sval, this.columns, Table.defaulSeparator, this.autoClean, this.autoRoot);
	}

	@Override
	public boolean addRowsAll(Object rows) {
		return Table.addRowsAllWithNull(rows, this.sval, this.columns, Table.defaulSeparator, this.autoClean, this.autoRoot);
	}

	@Override
	public void clear() {
		this.sval.clear();
	}

	@Override
	public boolean columnValue(Object row, Object column, E value) {
		String key = Table.tableJoiner.transform(new Pair<Object, Object>(row, column)).toString();
		if(this.sval.containsKey(key)){
			if(this.sval.get(key)!=null){
				this.sval.get(key).add(value);
			}
			else{
				ComCollection<E> coll = new ComCollection<E>();
				coll.add(value);
				this.sval.put(key, coll);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean contains(Object row) {
		return this.sval.containsKey(Table.tableJoiner.transform(new Pair<Object, Object>(null, row)).toString());
	}

	@Override
	public boolean contains(Object row, Object column) {
		return this.sval.containsKey(Table.tableJoiner.transform(new Pair<Object, Object>(row, column)).toString());
	}

	@Override
	public Map<String, E> get(Object row) {
		Map<String, E> ret = new HashMap<String, E>();
		Collection<String> columns = IsPath.GET_SUB_PATHS(Table.defaulSeparator, Table.tableJoiner.transform(new Pair<Object, Object>(null, row)), this.sval.keySet());
		for(String s : columns){
			if(this.sval.get(s)==null){
				ret.put(s, null);
			}
			else{
				ret.put(s, this.sval.get(s).getFirst());
			}
		}
		return ret;
	}

	@Override
	public E get(Object row, Object column) {
		ComCollection<E> coll = this.sval.get(Table.tableJoiner.transform(new Pair<Object, Object>(row, column)).toString());
		if(coll!=null){
			return coll.getFirst();
		}
		return null;
	}

	/**
	 * Returns the values of a cell identified by row and column
	 * @param row row of the cell
	 * @param column column of the cell
	 * @return collection of values of the cell
	 */
	public Collection<E> getMulti(Object row, Object column){
		return this.sval.get(Table.tableJoiner.transform(new Pair<Object, Object>(row, column)).toString());
	}

	@Override
	public FlatMultiTable<E> getCopy() {
		FlatMultiTable<E> ret = new FlatMultiTable<E>(this.strategy, this.columns);
		ret.sval.putAll(this.sval);
		return ret;
	}

	/**
	 * Returns a complete row will all columns and asociated values.
	 * @param row row identifier
	 * @return complete row
	 */
	public Map<String, ComCollection<E>> getRow(Object row) {
		Map<String, ComCollection<E>> ret = new HashMap<String, ComCollection<E>>();
		Collection<String> columns = IsPath.GET_SUB_PATHS(Table.defaulSeparator, Table.tableJoiner.transform(new Pair<Object, Object>(null, row)), this.sval.keySet());
		for(String s : columns){
			ret.put(s, this.sval.get(s));
		}
		return ret;
	}

	/**
	 * Initializes the table.
	 * @param strategy stray for the internal list
	 * @param columns collection of columns
	 */
	private void init(IsSetStrategy strategy, Collection<IsAttributeKey> columns){
		this.sval = new HashMap<String, ComCollection<E>>();

		if(strategy!=null){
			this.strategy = strategy;
		}
		else{
			this.strategy = SetStrategy.LINKED_HASH_SET;
		}
		this.columns = this.strategy.get(this.columns=null);

		if(columns==null){
			this.columns.add(EAttributeKeys.DEFAULT);
		}
		else{
			for(IsAttributeKey key:columns){
				if(key!=null){
					this.columns.add(key);
				}
			}
		}
	}

	@Override
	public boolean isEmpty() {
		return this.sval.isEmpty();
	}

	@Override
	public Set<String> keys() {
		return this.sval.keySet();
	}

	@Override
	public boolean removeColumnValue(Object row, Object column) {
		return this.columnValue(row, column, null);
	}

	@Override
	public boolean removeRow(Object row) {
		return Table.removeTableRow(row, this.sval, Table.defaulSeparator, this.autoClean, this.autoRoot);
	}

	@Override
	public int size() {
		return this.sval.size();
	}

	@Override
	public String toString(){
		return Skb_CollectionTransformer.MAP_TO_TEXT(this.sval);
	}
}
