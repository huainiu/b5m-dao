package com.b5m.dao.domain.item;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.b5m.dao.domain.ColType;
import com.b5m.dao.domain.table.Entity;
import com.b5m.dao.domain.table.EntityField;

public class Chain implements SqlItem{
	
	private List<Prop> props;
	
	public Chain(){
		props = new ArrayList<Prop>();
	}
	
	public static Chain _new(){
		return new Chain();
	}
	
	public static Chain _new(String name, Object value){
		Chain chain = _new();
		chain.props.add(Prop.newInstance(name, value));
		return chain;
	}
	
	public void add(String name, Object value){
		this.props.add(Prop.newInstance(name, value));
	}

	@Override
	public void joinSql(Entity<?> en, StringBuilder sb) {
		sb.append("UPDATE ").append(en.getTableName()).append(" ").append("SET") ;
		int length = this.props.size();
		for(int index = 0; index < length; index++){
			Prop prop = this.props.get(index);
			EntityField field = en.getField(prop.getName());
			sb.append(" ").append(field.getColumnName()).append("=?");
			if(index < length - 1){
				sb.append(",");
			}
		}
		if(sb.toString().endsWith(",")){
			sb.deleteCharAt(sb.length() - 1);
		}
	}

	@Override
	public void joinParams(Entity<?> en, List<Object> params, List<ColType> colTypes, List<Class<?>> fieldTyps) {
		int length = this.props.size();
		for(int index = 0; index < length; index++){
			Prop prop = this.props.get(index);
			params.add(prop.getValue());
			EntityField field = en.getField(prop.getName());
			fieldTyps.add(field.getTypeClass());
			colTypes.add(field.getColType());
		}
	}
	
}
