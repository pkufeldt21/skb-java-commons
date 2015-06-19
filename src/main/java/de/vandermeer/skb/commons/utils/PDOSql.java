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

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.vandermeer.skb.commons.collections.Tree;

/**
 * Implementation of the {@link PDO} interface.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.4 build 150619 (19-Jun-15) for Java 1.8
 */
public class PDOSql implements PDO {
	/** Local connection of this PDO object. */
	private Connection core;

	/** Local statement. */
	private Statement statement;

	/** Local result set. */
	private ResultSet result_set;

	@Override
	public void clearWarnings() throws SQLException {
		try{
			this.core.clearWarnings();
		}
		catch (SQLException e) {
			//logger.error("catched exception: "+e); //TODO
			throw e;
		}
	}

	@Override
	public void close() throws SQLException {
		try{
			this.core.close();
			this.statement.close();
			this.result_set.close();
		}
		catch (SQLException e) {
			//logger.error("catched exception: "+e); //TODO
			throw e;
		}
	}

	@Override
	public void commit() throws SQLException {
		try{
			this.core.commit();
		}
		catch (SQLException e) {
			//logger.error("catched exception: "+e); //TODO
			throw e;
		}
	}

	@Override
	public Statement createStatement() throws SQLException {
		Statement s = null;
		try{
			this.core.createStatement();
		}
		catch (SQLException e){
			//logger.error("catched exception: "+e); //TODO
			throw e;
		}
		return s;
	}

	@Override
	public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
		Statement s = null;
		try{
			this.core.createStatement(resultSetType, resultSetConcurrency);
		}
		catch (SQLException e){
			//logger.error("catched exception: "+e); //TODO
			throw e;
		}
		return s;
	}

	@Override
	public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
		Statement s = null;
		try{
			this.core.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
		}
		catch (SQLException e){
			//logger.error("catched exception: "+e); //TODO
			throw e;
		}
		return s;
	}

	@Override
	public ArrayList<String> getColumns() {
		if(this.result_set!=null){
			try {
				ResultSetMetaData rsmd = this.result_set.getMetaData();
				int colC = rsmd.getColumnCount()+1;
				ArrayList<String> columns = new ArrayList<String>();
				for(int i=1; i<colC; i++){
					columns.add(rsmd.getColumnName(i));
				}
				return columns;
			}
			catch (Exception e) {
				//logger.warn("Could not extract ResultSet MetaData\n --> USER WARNING"); //TODO
				//logger.error("exception while looking for columns: "+e); //TODO
				return null;
			}
		}
		//logger.warn("Something wrong with result set" + "\n --> USER WARNING"); //TODO
		return null;
	}

	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		DatabaseMetaData dmd = null;
		try{
			this.core.getMetaData();
		}
		catch (SQLException e){
			//logger.error("catched exception: "+e); //TODO
			throw e;
		}
		return dmd;
	}

	@Override
	public ResultSet query(ArrayList<String> sel, String table, String whe, String ord) {
		String select = null;
		if(sel!=null){
			select=new String();
			for(int i=0; i<sel.size(); i++){
				if(i>0){
					select += ",";
				}
				select += sel.get(i);
			}
			return this.query(select, table, whe, ord);
		}
		return null;
	}

	@Override
	public ResultSet query(String sel, String table, String whe, String ord) {
		this.result_set = null;
		String select = null;
		if(sel.length()>0){
			select = sel;
		}
		else{
			;//logger.error("Empty Select string!"+"\n--> USER ERROR"); //TODO
		}

		if(select==null && table==null){
			return null;
		}

		String sql=new String("SELECT "+select+" FROM "+table);
		if(whe!=null && whe.length()>0){
			sql += " WHERE "+whe;
		}
		if(ord!=null && ord.length()>0){
			sql+=" ORDER BY \""+ord+"\"";
		}
		try {
			this.statement = this.core.createStatement();
			this.result_set = statement.executeQuery(sql);
		}
		catch (Exception e) { 
			//logger.error("create/execute exception: "+e); //TODO
		}
		return this.result_set;
	}

	@Override
	public Tree<Object> semanticQuery(Tree<Object> query, String from) {
//		TcMultiMap ret=new MapBuilderImpl().buildTree();
//
//		//select string
//		SkbType f=query.getFirst("find");
//		if((f instanceof TcListTop)&&((TcListTop)f).size()>0)
//			query.put("find", StringUtils.join(((TcListTop)query.get("find")).toArray(),","));
//		if(!(query.get("find").toString().length()>0))
//			query.put("find", "*");
//
//		//where string
//		String where="";
//		//TODO was CtMapTree, now getFirst, logic is wrong here
//		f=query.getFirst("equals");
//		if((f instanceof TcMultiMap)&&((TcMultiMap)f).size()>0){
//			TcMultiMap _a=(TcMultiMap)f;
//			Set<String> o_set = _a.keySet();
//			Iterator<String> key_it = o_set.iterator();
//			while(key_it.hasNext()){
//				String key=key_it.next();
//				String val=_a.get(key).toString();
//				where+=" \""+key+"\" = \""+val+"\"";
//			}
//		}
//
//		//order string
//		String order=query.get("sort").toString();
//
//		ResultSet rs=this.query(query.get("find").toString(), from, where, order);
//		ArrayList<String> cols=this.getColumns();
//		try{
//			while(rs.next()){
//				for(int i=0;i<cols.size();i++){
//					//if we don't look for anything, create a list, otherwise, only the columns of the searched row(s)
//					if(where=="")
//						ret.put(new ArrayList<String>(Arrays.asList(rs.getString("key"), cols.get(i))), rs.getString(cols.get(i)));
//					else
//						ret.put(new ArrayList<String>(Arrays.asList(cols.get(i))), rs.getString(cols.get(i)));
//				}
//			}
//		} catch (Exception e) {
//			//logger.error("exception while filling result map: "+e); //TODO
//		}
//		return ret;
		return null;
	}

	@Override
	public PDO getCopy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return "pdo";
	}
}
