package com.b5m.dao.domain.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.b5m.dao.exception.DaoException;
/**
 * @Company B5M.com
 * @description
 * 
 * @author echo
 * @since 2013-7-12
 * @email echo.weng@b5m.com
 */
public class Entity<T>{
	private Map<String, String> nameQueryMap;
	
	private Map<String, EntityField> byDB;
	
	private Map<String, EntityField> byPojo;
	
	private List<EntityField> fields;
	
	private EntityField id;
	
	private EntityField name;
	
	private Class<T> type;
	
	private String tableName;
	
	public Entity(Class<T> type, String tableName){
		this.type = type;
		this.tableName = tableName;
		this.byPojo = new HashMap<String, EntityField>();
		this.byDB = new HashMap<String, EntityField>();
		this.fields = new ArrayList<EntityField>(5);
		nameQueryMap = new HashMap<String, String>(5);
	}

	/**
	 * 增加映射字段
	 * 
	 * @param field
	 *            数据库实体字段
	 */
	public void addEntityField(EntityField field) {
		if (field.isId()) id = field;
		else if (field.isName()) name = field;
		byPojo.put(field.getName(), field);
		byDB.put(field.getColumnName(), field);
		fields.add(field);
	}
	
	public void putNameQueryMap(String name, String sql){
		nameQueryMap.put(name, sql);
	}
	
	public String getSqlQueryName(String queryName){
		String sql = nameQueryMap.get(queryName);
		if(StringUtils.isEmpty(sql)){
			throw new DaoException("no found sql for query name[" + queryName + "]");
		}
		return sql;
	}
	
	public Class<T> getType() {
		return this.type;
	}

	public String getTableName() {
		return this.tableName;
	}

	public EntityField getField(String name) {
		return byPojo.get(name);
	}

	public EntityField getColumn(String name) {
		return byDB.get(name);
	}

	public EntityField getNameField() {
		return name;
	}

	public EntityField getIdField() {
		return id;
	}

	public List<EntityField> getFields(){
		return this.fields;
	}
}
