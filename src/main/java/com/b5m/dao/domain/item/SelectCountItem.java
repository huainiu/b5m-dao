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
public class SelectCountItem implements SqlItem{
	
	public SelectCountItem(){}

	public static SelectCountItem newInstance(){
		return new SelectCountItem();
	}

	@Override
	public void joinSql(Entity<?> en, StringBuilder sb) {
		sb.append("SELECT COUNT(*) FROM ").append(en.getTableName()).append(" ");
	}

	@Override
	public void joinParams(Entity<?> en, List<Object> params, List<ColType> colTypes, List<Class<?>> fieldTyps) {
		//无参数
	}

}
