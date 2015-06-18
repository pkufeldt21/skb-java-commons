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

import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.misc.Pair;
import org.apache.commons.lang3.text.StrBuilder;

import de.vandermeer.skb.base.Skb_Transformer;
import de.vandermeer.skb.base.utils.Skb_ObjectUtils;
import de.vandermeer.skb.categories.IsPath;
import de.vandermeer.skb.categories.kvt.IsAttributeKey;
import de.vandermeer.skb.composite.CompositeObject;

/**
 * A Table.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.4-SNAPSHOT build 150618 (18-Jun-15) for Java 1.8
 */
public interface Table<E> extends CompositeObject {

	/** The default separator for table paths set to "/" */
	public static String defaulSeparator="/";

	public static final Skb_Transformer<Pair<Object, Object>, StrBuilder> tableJoiner = IsPath.JOIN_PATH_ELEMENTS(Table.defaulSeparator, true, true);

	/**
	 * Adds a row to the table.
	 * @param row row name
	 * @return true if the row was added, false otherwise
	 */
	boolean addRow(Object row);

	/**
	 * Adds rows to a table.
	 * @param rows rows, if a collection or array each member will be treated as an individual row
	 * @return true if rows were added, false otherwise
	 */
	boolean addRowsAll(Object rows);

	/**
	 * Clears the table.
	 * Removes all entries of the table, but keeps the set columns.
	 */
	void clear();

	/**
	 * Sets the value of a cell.
	 * @param row row name
	 * @param column column name
	 * @param value new value for the cell
	 * @return true if successful, false otherwise (i.e. cell does not exist)
	 */
	boolean columnValue(Object row, Object column, E value);

	/**
	 * Tests if a row exists
	 * @param row row name
	 * @return true if the row exists, false otherwise
	 */
	boolean contains(Object row);

	/**
	 * Tests if a cell exists in the table.
	 * @param row row name
	 * @param column column name
	 * @return true if cell exists, false otherwise
	 */
	boolean contains(Object row, Object column);

	/**
	 * Returns a complete row (all its columns with they value)
	 * @param row row name
	 * @return map with column names as key and their associated values as values
	 */
	Map<String, E> get(Object row);

	/**
	 * Returns the value of a cell (row and column)
	 * @param row row name
	 * @param column column name
	 * @return value of the cell
	 */
	E get(Object row, Object column);

	/**
	 * Tests if the table is empty.
	 * @return true if the table is empty, false otherwise
	 */
	boolean isEmpty();

	/**
	 * Returns a set view for all keys in the table (rows and columns)
	 * @return set with all rows and columns (not ordered)
	 */
	Set<String> keys();

	/**
	 * Removes the value of a column, that is sets it to null
	 * @param row row
	 * @param column column
	 * @return true if the value was remvoed (reset), false otherwise
	 */
	boolean removeColumnValue(Object row, Object column);

	/**
	 * Removes a complete row, including all its columns and their values
	 * @param row row to be removed
	 * @return true if the row does not exist in the table, false otherwise
	 */
	boolean removeRow(Object row);

	/**
	 * Returns the size of the table.
	 * @return size as an integer
	 */
	int size();

	/**
	 * Adds a complete row with null values for each column.
	 * @param row name of the row
	 * @param map map to add the row to
	 * @param columns set of columns for the row
	 * @param separator path separator
	 * @param doClean clean the final path from excessive elements
	 * @param setRoot set a root separator to the final path
	 * @return true on success, false otherwise
	 */
	public static boolean addRowWithNull(Object row, Map<String, ?> map, Set<IsAttributeKey> columns, String separator, Boolean doClean, Boolean setRoot){
		if(row==null || map==null || columns==null){
			return false;
		}
		StrBuilder fqpn = IsPath.JOIN_PATH_ELEMENTS(separator, doClean, setRoot).transform(new Pair<Object, Object>(null, row));
		if(IsPath.PATH_TO_LEVELS(separator).transform(fqpn)==1){
			String rowKey = IsPath.forMaxDepth(fqpn, 1, separator).toString();
			if(!map.containsKey(rowKey)){
				for(IsAttributeKey col : columns){
					map.put(rowKey + separator + col.key(), null);
				}
				map.put(rowKey, null);
				return true;
			}
		}
		return false;
	}

	/**
	 * Adds a complete set of rows with null values for each column.
	 * @param rows set of rows
	 * @param map map to add the row to
	 * @param columns set of columns for the row
	 * @param separator path separator
	 * @param doClean clean the final path from excessive elements
	 * @param setRoot set a root separator to the final path
	 * @return true on success, false otherwise
	 */
	public static boolean addRowsAllWithNull(Object rows, Map<String, ?> map, Set<IsAttributeKey> columns, String separator, Boolean doClean, Boolean setRoot){
		if(rows==null || map==null || columns==null){
			return false;
		}
		Object[] r = Skb_ObjectUtils.CONVERT(rows, Object[].class, null, null);
		if(r!=null){
			for(Object row : r){
				Table.addRowWithNull(row, map, columns, separator, doClean, setRoot);
			}
			return true;
		}
		return false;
	}

	/**
	 * Removes a complete table row.
	 * @param row row to be removed
	 * @param map map to remove the row from
	 * @param separator path separator
	 * @param doClean clean the final path from excessive elements
	 * @param setRoot set a root separator to the final path
	 * @return true on success, false otherwise
	 */
	public static boolean removeTableRow(Object row, Map<String, ?> map, String separator, Boolean doClean, Boolean setRoot){
		if(row==null || map==null){
			return false;
		}

		StrBuilder fqpn = IsPath.JOIN_PATH_ELEMENTS(separator, doClean, setRoot).transform(new Pair<Object, Object>(null, row));
		if(IsPath.PATH_TO_LEVELS(separator).transform(fqpn)==1){
			return Tree.removeNode(fqpn, map, separator);
		}
		return false;
	}
}
