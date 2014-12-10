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

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;

import de.vandermeer.skb.commons.collections.ComCollection;
import de.vandermeer.skb.commons.collections.FlatMultiTree;
import de.vandermeer.skb.composite.SkbObject;
import de.vandermeer.skb.composite.specialobject.NONull;

/**
 * Reads JSON and creates SKB Java Objects.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.3-SNAPSHOT build 141210 (10-Dec-14) for Java 1.8
 */
public class Json2Collections {
	protected ArrayList<String> path;

	/** Creates a new conversion object */
	public Json2Collections(){
		this.path = new ArrayList<String>();
	}

	/**
	 * Reads the given JSON file and logs errors.
	 * @param file JSON file
	 * @return a map with information from the JSON file or null in case of errors 
	 */
	public Object read(File file) {
		if(file!=null && file.canRead()){
			try{
				return this.read(new Scanner(file));
			}
			catch (Exception ignore){}
		}
		return null;
	}

	/**
	 * Reads JSON from the input scanner, transforms into an SKB map and logs errors.
	 * 
	 * This method parses the <code>input</code> and removes every line starting with either of the two possible single line comments: '//' and '#'.
	 * It then calls s2o with the altered input.
	 * @param input scanner wit JSON specification
	 * @return a tree with information from the JSON file or null in case of errors
	 */
	public Object read(Scanner input) {
		Object ret = null;
		if(input!=null){
			String content = new String();
			try{
				while(input.hasNextLine()){
					String line = input.nextLine();
					if(!StringUtils.startsWithAny(line.trim(), new String[]{"//","#"}))
						content += line.trim();
				}
			}
			catch (Exception ignore){}
			ret = this.s2o(content);
		}
		return ret;
	}

	/**
	 * Reads the JSON from the given URL and logs errors.
	 * @param url URL to resource
	 * @return a tree with information from the JSON file or null in case of errors
	 */
	public Object read(URL url) {
		try{
			return this.read(new Scanner(url.openStream()));
		}
		catch (Exception ignore){}
		return null;
	}

	/**
	 * Reads the JSON from the given input stream and logs errors.
	 * @param is JSON input stream
	 * @return a tree with information from the JSON file or null in case of errors
	 */
	public Object read(InputStream is) {
		try{
			return this.read(new Scanner(is));
		}
		catch (Exception ignore){}
		return null;
	}

	/**
	 * Reads the JSON from the given readable and logs errors.
	 * @param r JSON readable
	 * @return a tree with information from the JSON file or null in case of errors
	 */
	public Object read(Readable r) {
		try{
			return this.read(new Scanner(r));
		}
		catch (Exception ignore){}
		return null;
	}

	/**
	 * Transforms the given JSON string into a SkbType object
	 * @param content JSON string
	 * @return The return value depends on the JSON specification. It can be a TSArrayList or a TSMapLH for composite items or a
	 * TSString, TSBoolean, TSInteger, TSDouble or TSFloat for atomic items. The method is using recursion to parse each part of the JSON string and construct
	 * a single TSBase object.
	 */
	public Object s2o(String content) {
		try{
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
			JsonNode rootNode = mapper.readValue(content, JsonNode.class);
			this.path.clear();
			return this.traverse(rootNode);
		}
		catch (Exception ignore){}
		return null;
	}

	/**
	 * Recurses through the given node and constructs a TSBase return.
	 * @param node starting node for recursion
	 * @return {@link SkbObject} object
	 */
	private Object traverse(JsonNode node){
		if(node.isArray()){
			ComCollection<Object> ret = new ComCollection<Object>();
			Iterator<JsonNode> valFields = node.getElements();
			while(valFields.hasNext()){
				ret.add(this.traverse(valFields.next()));
			}
			return ret;
		}
		else if(node.isObject()){
			FlatMultiTree<Object> ret = new FlatMultiTree<Object>();

			Iterator<JsonNode> valFields = node.getElements();
			Iterator<String> keyFields = node.getFieldNames();
			while(keyFields.hasNext() && valFields.hasNext()){
				String key = keyFields.next();
				this.path.add(key);
				this.traverse(valFields.next(), ret);
				this.path.remove(this.path.size()-1);
			}
			return ret;
		}
		else if(node.isTextual()){
			return node.getTextValue();
		}
		else if(node.isBoolean()){
			return node.getBooleanValue();
		}
		else if(node.isIntegralNumber()){
			return node.getIntValue();
		}
		else if(node.isDouble()){
			return node.getDoubleValue();
		}
		else if(node.isFloatingPointNumber()){
			return node.getDoubleValue();
		}
		else if(node.isNull()){
			return NONull.get;
		}

		return null;
	}

	/**
	 * Traverses a node and returns an SKB type.
	 * @param node JSON node to process
	 * @param tree SKB type
	 */
	private void traverse(JsonNode node, FlatMultiTree<Object> tree){
		if(tree==null){
			return;
		}

		if(node.isArray()){
			Iterator<JsonNode> valFields = node.getElements();
			while(valFields.hasNext()){
				this.traverse(valFields.next(), tree);
			}
		}
		else if(node.isObject()){
			Iterator<JsonNode> valFields = node.getElements();
			Iterator<String> keyFields = node.getFieldNames();
			while(keyFields.hasNext() && valFields.hasNext()){
				String key = keyFields.next();
				this.path.add(key);
				this.traverse(valFields.next(), tree);
				this.path.remove(this.path.size()-1);
			}
		}
		else if(node.isTextual()){
			tree.addNodeWithValue(this.path, node.getTextValue());
		}
		else if(node.isBoolean()){
			tree.addNodeWithValue(this.path, node.getBooleanValue());
		}
		else if(node.isIntegralNumber()){
			tree.addNodeWithValue(this.path, node.getIntValue());
		}
		else if(node.isDouble()){
			tree.addNodeWithValue(this.path, node.getDoubleValue());
		}
		else if(node.isFloatingPointNumber()){
			tree.addNodeWithValue(this.path, node.getDoubleValue());
		}
		else if(node.isNull()){
			tree.addNodeWithValue(this.path, NONull.get);
		}
		return;
	}
}
