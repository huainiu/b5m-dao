package com.b5m.dao.domain.item;

import java.util.List;

import com.b5m.dao.domain.ColType;
import com.b5m.dao.domain.table.Entity;

public class QueryNameItem implements SqlItem{
	
	private Object[] objs;
	
	private String queryName;
	
	public QueryNameItem(String queryName, Object[] objs){
		this.objs = objs;
		this.queryName = queryName;
	}
	
	public static QueryNameItem newInstance(String queryName, Object[] objs){
		return new QueryNameItem(queryName, objs);
	}

	@Override
	public void joinSql(Entity<?> en, StringBuilder sb) {
		sb.append(en.getSqlQueryName(queryName));
	}

	@Override
	public void joinParams(Entity<?> en, List<Object> params, List<ColType> colTypes, List<Class<?>> fieldTyps) {
		if(objs == null) return;
		for(Object obj : objs){
			params.add(obj);
		}
	}
	
}
