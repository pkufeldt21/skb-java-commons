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

/**
 * Collection types (collection, tables, trees) as implementations of {@link de.vandermeer.skb.composite.CompositeObject} objects.
 * 
 * This package provides a number of collection-like SKB composite types, along with some utility classes.
 * The two main types are a tree and a table. Both come with an interface and a common implementation. Both support generics.
 * 
 * <p>
 * The common tree implementation is essentially a facade for a map, which uses the map's keys as path in a tree.
 * The general mechanism is similar to a file system. The common table implementation is then realised as a tree
 * with restricted maximum depth of the path (2).
 * </p>
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.4 build 150619 (19-Jun-15) for Java 1.8
 */
package de.vandermeer.skb.commons.collections;