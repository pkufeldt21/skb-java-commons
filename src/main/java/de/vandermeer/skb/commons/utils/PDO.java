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

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.vandermeer.skb.commons.collections.Tree;

/**
 * A Portable Database Object.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.3-SNAPSHOT build 141210 (10-Dec-14) for Java 1.8
 */
public interface PDO {
	/**
	 * Clears all warnings.
	 * @throws SQLException from library call
	 */
	public void clearWarnings() throws SQLException;

	/**
	 * Closes the local connection.
	 * Connections are: the connection to the database, the result set and the statement.
	 * @throws SQLException from library call
	 */
	public void close() throws SQLException;

	/**
	 * Calls commit on the database.
	 * @throws SQLException from library call
	 */
	public void commit() throws SQLException;

	/**
	 * Creates an SQL statement.
	 * @return new statement
	 * @throws SQLException from library call
	 */
	public Statement createStatement() throws SQLException;

	/**
	 * Creates an SQL statement.
	 * @param resultSetType
	 * @param resultSetConcurrency
	 * @return new statement
	 * @throws SQLException
	 */
	public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException;

	/**
	 * Creates an SQL statement.
	 * @param resultSetType
	 * @param resultSetConcurrency
	 * @param resultSetHoldability
	 * @return new statement
	 * @throws SQLException
	 */
	public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException;

	/**
	 * Returns columns of a result set by parsing the meta data.
	 * @return detected columns
	 */
	public ArrayList<String> getColumns();

	/**
	 * Returns SQL metadata
	 * @return metadata
	 * @throws SQLException
	 */
	public DatabaseMetaData getMetaData() throws SQLException;

	/**
	 * Queries the database.
	 * @param sel select parameters
	 * @param table table to query
	 * @param whe where parameter
	 * @param ord order parameter
	 * @return query result
	 */
	public ResultSet query(ArrayList<String> sel, String table, String whe, String ord);

	/**
	 * Queries the database.
	 * @param sel select parameter
	 * @param table table to query
	 * @param whe where parameter
	 * @param ord order parameter
	 * @return query result
	 */
	public ResultSet query(String sel, String table, String whe, String ord);

	//TODO explain the semantic query!
	/**
	 * Issues a semantic query, with specialised parameters.
	 * @param query query object
	 * @param from 
	 * @return results of the query
	 */
	public Tree<Object> semanticQuery(Tree<Object> query, String from);

	/**
	 * 
	 * @return
	 */
	public PDO getCopy();
}
