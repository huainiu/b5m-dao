package com.b5m.dao.domain.item;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.b5m.dao.domain.ColType;
import com.b5m.dao.domain.table.Entity;
import com.b5m.dao.domain.table.EntityField;
import com.b5m.dao.exception.DaoException;
import com.b5m.dao.utils.LogUtils;
/**
 * @Company B5M.com
 * @description
 * 
 * @author echo
 * @since 2013-7-8
 * @email echo.weng@b5m.com
 */
@SuppressWarnings("unchecked")
public class UpdateItem implements SqlItem{
	/**
	 * 操作对象
	 */
	private Object o;
	
	public UpdateItem(Object o){
		this.o = o;
	}
	
	public static UpdateItem newInstance(Object o){
		return new UpdateItem(o);
	}

	@Override
	public void joinSql(Entity<?> en, StringBuilder sb) {
		sb.append("UPDATE ").append(en.getTableName()).append(" ").append("SET") ;
		List<EntityField> entityFields = en.getFields(); 
		int length = entityFields.size();
		String primaykey = "";
		for(int index = 0; index < length; index++ ){
			EntityField field = entityFields.get(index);
			if(field.isId()){
				primaykey = field.getColumnName();
				continue;
			}
			if(StringUtils.isEmpty(primaykey) && field.isName()){
				primaykey = field.getColumnName();
				continue;
			}
			sb.append(" ").append(field.getColumnName()).append("=?");
			if(index < length - 1){
				sb.append(",");
			}
		}
		if(sb.toString().endsWith(",")){
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append(" WHERE ").append(primaykey).append(" = ? ");
	}

	@Override
	public void joinParams(Entity<?> en, List<Object> params, List<ColType> colTypes, List<Class<?>> fieldTyps) {
		if(o instanceof List){
			List<Object> os = (List<Object>) o;
			for(Object object : os){
				List<Object> oneParams = new ArrayList<Object>();
				setValue(en, oneParams, object);
				params.add(oneParams);
			}
		}else{
			setValue(en, params, o);
		}
		setType(en, colTypes, fieldTyps);
	}
	
	public void setValue(Entity<?> en, List<Object> params, Object operationObj){
		Object primaryValue = null;
		for(EntityField field : en.getFields()){
			Object value = null;
			try {
				value = field.getReadMethod().invoke(operationObj, null);
			} catch (Exception e) {
				LogUtils.error(this.getClass(), e);
				throw new DaoException(e);
			}
			if(field.isId() || field.isName()){
				primaryValue = value;
			}else{
				params.add(value);
			}
		}
		params.add(primaryValue);
	}
	
	public void setType(Entity<?> en, List<ColType> colTypes, List<Class<?>> fieldTyps){
		ColType primaryColType = null;
		Class<?> primaryTypeClass = null;
		for(EntityField field : en.getFields()){
			if(field.isId() || field.isName()){
				primaryColType = field.getColType();
				primaryTypeClass = field.getTypeClass();
			}else{
				colTypes.add(field.getColType());
				fieldTyps.add(field.getTypeClass());
			}
		}
		colTypes.add(primaryColType);
		fieldTyps.add(primaryTypeClass);
	}
	
}
