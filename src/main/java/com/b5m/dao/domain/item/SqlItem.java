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
public interface SqlItem {
	
	void joinSql(Entity<?> en, StringBuilder sb);
	
	void joinParams(Entity<?> en, List<Object> params, List<ColType> colTypes, List<Class<?>> fieldTyps);
}
