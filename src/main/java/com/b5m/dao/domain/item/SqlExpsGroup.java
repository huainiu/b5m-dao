package com.b5m.dao.domain.item;

import java.util.ArrayList;
import java.util.List;

import com.b5m.dao.domain.ColType;
import com.b5m.dao.domain.cnd.Op;
import com.b5m.dao.domain.table.Entity;
/**
 * @Company B5M.com
 * @description
 * 
 * @author echo
 * @since 2013-7-8
 * @email echo.weng@b5m.com
 */
public class SqlExpsGroup implements SqlItem{
	private List<ISqlExp> sqlExps;
	
	private List<SqlExpsGroup> sqlExpsGroups;
	
	public SqlExpsGroup(){
		sqlExps = new ArrayList<ISqlExp>(5);
		sqlExpsGroups = new ArrayList<SqlExpsGroup>(1);
	}
	
	public SqlExpsGroup and(String name, Op op, Object value){
		sqlExps.add(SqlExp.newInstance(name, op, value));
		return this;
	}
	
	public SqlExpsGroup and(ISqlExp sqlExp){
		sqlExps.add(sqlExp);
		return this;
	}

	public List<SqlExpsGroup> getSqlExpsGroups() {
		return sqlExpsGroups;
	}
	
	public SqlExpsGroup addSqlExpsGroup(SqlExpsGroup sqlExpsGroup){
		sqlExpsGroups.add(sqlExpsGroup);
		return this;
	}

	public void setSqlExpsGroups(List<SqlExpsGroup> sqlExpsGroups) {
		this.sqlExpsGroups = sqlExpsGroups;
	}

	@Override
	public void joinSql(Entity<?> en, StringBuilder sb) {
		for(ISqlExp sqlExp : sqlExps){
			sqlExp.joinSql(en, sb);
		}
		for(SqlExpsGroup sqlExpsGroup : sqlExpsGroups){
			sqlExpsGroup.joinSql(en, sb);
		}
	}

	@Override
	public void joinParams(Entity<?> en, List<Object> params, List<ColType> colTypes, List<Class<?>> fieldTypes) {
		for(ISqlExp sqlExp : sqlExps){
			sqlExp.joinParams(en, params, colTypes, fieldTypes);
		}
		for(SqlExpsGroup sqlExpsGroup : sqlExpsGroups){
			sqlExpsGroup.joinParams(en, params, colTypes, fieldTypes);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sqlExps == null) ? 0 : sqlExps.hashCode());
		result = prime * result
				+ ((sqlExpsGroups == null) ? 0 : sqlExpsGroups.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SqlExpsGroup other = (SqlExpsGroup) obj;
		if (sqlExps == null) {
			if (other.sqlExps != null)
				return false;
		} else if (!sqlExps.equals(other.sqlExps))
			return false;
		if (sqlExpsGroups == null) {
			if (other.sqlExpsGroups != null)
				return false;
		} else if (!sqlExpsGroups.equals(other.sqlExpsGroups))
			return false;
		return true;
	}
	
}
