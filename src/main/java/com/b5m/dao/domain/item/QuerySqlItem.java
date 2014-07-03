package com.b5m.dao.domain.item;

import java.util.List;

import com.b5m.dao.domain.ColType;
import com.b5m.dao.domain.table.Entity;

public class QuerySqlItem implements SqlItem{
	
	private Object[] objs;
	
	private String sql;
	
	public QuerySqlItem(String sql, Object[] objs){
		this.objs = objs;
		this.sql = sql;
	}
	
	public static QuerySqlItem newInstance(String sql, Object[] objs){
		return new QuerySqlItem(sql, objs);
	}

	@Override
	public void joinSql(Entity<?> en, StringBuilder sb) {
		sb.append(sql);
	}

	@Override
	public void joinParams(Entity<?> en, List<Object> params, List<ColType> colTypes, List<Class<?>> fieldTyps) {
		if(objs == null) return;
		for(Object obj : objs){
			params.add(obj);
		}
	}
	
}
