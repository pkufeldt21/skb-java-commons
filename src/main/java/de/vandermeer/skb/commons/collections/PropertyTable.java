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

import de.vandermeer.skb.base.utils.Skb_ObjectUtils;
import de.vandermeer.skb.base.utils.Skb_TextUtils;
import de.vandermeer.skb.categories.IsPath;
import de.vandermeer.skb.collections.IsSetStrategy;
import de.vandermeer.skb.composite.SpecialObject;
import de.vandermeer.skb.composite.specialobject.NONull;
import de.vandermeer.skb.composite.specialobject.NOSuccess;
import de.vandermeer.skb.composite.specialobject.NullObject;
import de.vandermeer.skb.configuration.EAttributeKeys;

/**
 * Property table, pre-configured with relevant columns.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.4-SNAPSHOT build 150618 (18-Jun-15) for Java 1.8
 */
public class PropertyTable extends FlatTable<Object> {

	/**
	 * Constructor, initialises the property table with a strategy for handling its array.
	 * @param strategy property table array strategy
	 */
	public PropertyTable(IsSetStrategy strategy){
		super(strategy, EAttributeKeys.values());
	}

	/**
	 * Returns the value of the specified property.
	 * The value is determined by testing the CLI value (value set by command line), then the file value (value read from file/json)
	 * and last the default value. The first one that is not set to {@link SpecialObject} will be returned. If none is set, a {@link NONull}
	 * will be returned.
	 * @param property property name
	 * @return The value is determined by testing the CLI value (value set by command line), then the file value (value read from file/json)
	 * and last the default value. The first one that is not set to {@link SpecialObject} will be returned. If none is set, a {@link NONull}
	 * will be returned.
	 */
	public Object getPropertyValue(Object property) {
		Object ret;

		ret = this.getPropertyColumn(property, EAttributeKeys.VALUE_SET);
		if(!(ret instanceof SpecialObject)){
			return ret;
		}

		ret = this.getPropertyColumn(property, EAttributeKeys.VALUE_CLI);
		if(!(ret instanceof SpecialObject)){
			return ret;
		}

		ret = this.getPropertyColumn(property, EAttributeKeys.VALUE_FILE);
		if(!(ret instanceof SpecialObject)){
			return ret;
		}

		ret = this.getPropertyColumn(property, EAttributeKeys.VALUE_DEFAULT);
		if(!(ret instanceof SpecialObject)){
			return ret;
		}

		return NONull.get;
	}

	/**
	 * Returns a property value as given class.
	 * @param <T> type of the return value
	 * @param property property name
	 * @param clazz type for the value to be returned
	 * @return null if no property found or found value is not of the requested type, value otherwise
	 */
	public <T> T getPropertyValue(Object property, Class<T> clazz){
		return Skb_ObjectUtils.CONVERT(this.getPropertyValue(property), clazz);
	}

	/**
	 * Returns the default value of the specified property.
	 * @param property property name
	 * @return {@link NONull} if no value is set, actual value otherwise
	 */
	public Object getPropertyValueDefault(Object property) {
		return this.getPropertyColumn(property, EAttributeKeys.VALUE_DEFAULT);
	}

	/**
	 * Returns the command line value of the specified property.
	 * @param property property name
	 * @return {@link NONull} if no value is set, actual value otherwise
	 */
	public Object getPropertyValueCli(Object property) {
		return this.getPropertyColumn(property, EAttributeKeys.VALUE_CLI);
	}

	private Object getPropertyColumn(Object row, Object column){
		Object ret = this.get(row, column);
		if(ret==null){
			return NONull.get;
		}
		return ret;
	}

	/**
	 * Tests if the map has a specified property.
	 * @param property property name
	 * @return true if property is set, false otherwise
	 */
	public boolean hasProperty(Object property) {
		return this.contains(property);
	}

	/**
	 * Tests if the map has a specified property and a value for it.
	 * @param property property name
	 * @param col column to be checked for a value
	 * @return true if property is set and has a value, false otherwise
	 */
	public boolean hasPropertyValue(Object property, Object col) {
		Object v = this.get(property, col);
		if(v==null || (v instanceof SpecialObject)){
			return false;
		}
		return true;
	}

	/**
	 * Sets the command line (CLI) value of a property.
	 * @param property property name
	 * @param val command line value
	 */
	public void setPropertyValueCli(Object property, Object val) {
		this.columnValue(property, EAttributeKeys.VALUE_CLI, val);
	}

	/**
	 * Sets the default value of a property.
	 * @param property property name
	 * @param val value to be used as default
	 */
	public void setPropertyValueDefault(Object property, Object val) {
		this.columnValue(property, EAttributeKeys.VALUE_DEFAULT, val);
	}

	@Override
	public PropertyTable getCopy() {
		PropertyTable ret = new PropertyTable(this.strategy);
		ret.sval.putAll(this.sval);
		return ret;
	}

	/**
	 * Loads properties from a tree.
	 * @param path root path for property information in the tree
	 * @param rows rows to be loaded from the tree
	 * @param input property information
	 * @return {@link NOSuccess} if some properties have been loaded, {@link NONull} on error (rows null, tree null or nothing found)
	 */
	public NullObject loadFromTree(IsPath path, Iterable<?> rows, Object input){
		int i=0;
		if(input!=null && (input instanceof Tree<?>)&&rows!=null){
			Tree<?> tree = (Tree<?>)input;
			for(Object row : rows){
				if(row!=null && tree.containsNode(path.path(), row)){
					for(EAttributeKeys col : EAttributeKeys.values()){
						Object val = tree.getValue(new Object[]{path.path(), row}, col);
						if(val!=null && !(val instanceof SpecialObject)){
							this.columnValue(row, col, val);
							i++;
						}
					}
				}
			}
		}
		return (i==0)?NONull.get:NOSuccess.get;
	}

	@Override
	public String toString(){
		return Skb_TextUtils.MAP_TO_TEXT().transform(this.sval);
	}
}