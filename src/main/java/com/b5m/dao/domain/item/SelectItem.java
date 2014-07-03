package com.b5m.dao.domain.item;

import java.util.List;

import com.b5m.dao.domain.ColType;
import com.b5m.dao.domain.table.Entity;
import com.b5m.dao.domain.table.EntityField;
/**
 * @Company B5M.com
 * @description
 * 
 * @author echo
 * @since 2013-7-10
 * @email echo.weng@b5m.com
 */
public class SelectItem implements SqlItem{
	
	public SelectItem(){}

	public static SelectItem newInstance(){
		return new SelectItem();
	}

	@Override
	public void joinSql(Entity<?> en, StringBuilder sb) {
		List<EntityField> fields = en.getFields();
		int length = fields.size();
		sb.append("SELECT ");
		for(int index = 0; index < length; index++){
			EntityField field = fields.get(index);
			sb.append(field.getColumnName());
			if(index < length - 1){
				sb.append(", ");
			}
		}
		sb.append(" ").append("FROM").append(" ").append(en.getTableName());
	}

	@Override
	public void joinParams(Entity<?> en, List<Object> params, List<ColType> colTypes, List<Class<?>> fieldTyps) {
		//无参数
	}

}
