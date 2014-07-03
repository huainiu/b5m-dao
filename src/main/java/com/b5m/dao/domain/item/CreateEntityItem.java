package com.b5m.dao.domain.item;

import java.util.List;

import com.b5m.dao.domain.ColType;
import com.b5m.dao.domain.table.Entity;
/**
 * @Company B5M.com
 * @description
 * 
 * @author echo
 * @since 2013-7-10
 * @email echo.weng@b5m.com
 */
public class CreateEntityItem implements SqlItem{
	
	public CreateEntityItem(){}
	
	public static CreateEntityItem newInstance(){
		return new CreateEntityItem();
	}

	@Override
	public void joinSql(Entity<?> en, StringBuilder sb) {
		sb.append(" select * from ").append(en.getTableName()).append(" where 1 != 1");
	}
	
	@Override
	public void joinParams(Entity<?> en, List<Object> params, List<ColType> colTypes, List<Class<?>> fieldTyps) {
		//没有参数操作
	}
}
