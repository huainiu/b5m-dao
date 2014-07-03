package com.b5m.dao.domain.item;

import java.util.ArrayList;
import java.util.List;

import com.b5m.dao.domain.ColType;
import com.b5m.dao.domain.table.Entity;
import com.b5m.dao.domain.table.EntityField;
import com.b5m.dao.exception.DaoException;
import com.b5m.dao.utils.LogUtils;

public class InsertItem implements SqlItem{
	/**
	 * 操作对象
	 */
	private Object o;
	
	public InsertItem(Object o){
		this.o = o;
	}
	
	public static InsertItem newInstance(Object o){
		return new InsertItem(o);
	}

	@Override
	public void joinSql(Entity<?> en, StringBuilder sb) {
		sb.append("INSERT INTO ").append(en.getTableName()).append(" (");
		List<EntityField> entityFields = en.getFields(); 
		int length = entityFields.size();
		int last = -1;
		for(int index = 0; index < length; index++ ){
			EntityField field = entityFields.get(index);
			if( !(field.isId() && field.isAuto()) ){
				sb.append(field.getColumnName() );
				if(index < length - 1){
					sb.append(",");
				}
			}else{
				last = index;
			}
		}
		if(last == length - 1){
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append(")");
		
		sb.append(" VALUES (");
		if(last > -1) length = length - 1;
		for(int index = 0; index < length; index++){
			sb.append("?");
			if(index < length - 1){
				sb.append(", ");
			}
		}
		sb.append("); ");
	}

	@Override
	public void joinParams(Entity<?> en, List<Object> params, List<ColType> colTypes, List<Class<?>> fieldTyps) {
		if(o instanceof List){
			List<Object> list = (List<Object>) o;
			for(Object object : list){
				List<Object> oneParams = new ArrayList<Object>();
				setValue(en, oneParams, object);
				params.add(oneParams);
			}
		}else{
			setValue(en, params, o);
		}
		for(EntityField field : en.getFields()){
			if( !(field.isId() && field.isAuto()) ){
				colTypes.add(field.getColType());
				fieldTyps.add(field.getTypeClass());
			}
		}
	}
	
	public void setValue(Entity<?> en, List<Object> params, Object operationObject){
		for(EntityField field : en.getFields()){
			if( !(field.isId() && field.isAuto()) ){
				Object value = null;
				try {
					value = field.getReadMethod().invoke(operationObject, null);
				} catch (Exception e) {
					LogUtils.error(this.getClass(), e);
					throw new DaoException(e);
				}
				params.add(value);
			}
		}
	}

}
