package com.b5m.dao.impl;

import java.util.HashMap;
import java.util.Map;

import com.b5m.dao.EntityMaker;
import com.b5m.dao.annotation.Table;
import com.b5m.dao.domain.table.Entity;

/**
 * @Company B5M.com
 * @description
 * 
 * @author echo
 * @since 2013-7-12
 * @email echo.weng@b5m.com
 */
@SuppressWarnings("unchecked")
public class EntityHolder {
	private EntityMaker entityMaker;
	
	private Map<Class<?>, Entity<?>> map;
	
	public EntityHolder(EntityMaker entityMaker){
		this.entityMaker = entityMaker;
		map = new HashMap<Class<?>, Entity<?>>();
	}
	
	public <T> Entity<T> getEntityBy(Object obj){
		Class<T> clazz = (Class<T>) obj.getClass();
		return getEntity(clazz);
	}
	
	public <T> Entity<T> getEntity(Class<T> type){
		Entity<T> entity = (Entity<T>) map.get(type);
		if(entity != null){
			return entity;
		}
		entity = entityMaker.make(type);
		map.put(type, entity);
		return entity;
	}
	
	/**
	 * @description
	 * 判断是否是实体类
	 * @return
	 * @return boolean
	 * @date 2013-7-15
	 * @author xiuqing.weng
	 */
	public boolean isEntityClass(Class<?> type){
		Table table = type.getAnnotation(Table.class);
		return table == null ? false : true;
	}
}
