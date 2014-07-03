package com.b5m.dao.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.b5m.dao.EntityMaker;
import com.b5m.dao.annotation.Column;
import com.b5m.dao.annotation.Id;
import com.b5m.dao.annotation.Name;
import com.b5m.dao.annotation.NamedQueries;
import com.b5m.dao.annotation.NamedQuery;
import com.b5m.dao.annotation.Table;
import com.b5m.dao.domain.ColType;
import com.b5m.dao.domain.table.Entity;
import com.b5m.dao.domain.table.EntityField;
import com.b5m.dao.exception.DaoException;
import com.b5m.dao.utils.ReflectionUtils;
/**
 * @Company B5M.com
 * @description
 * 
 * @author echo
 * @since 2013-7-10
 * @email echo.weng@b5m.com
 */
public class EntityMakerImpl implements EntityMaker {

	@Override
	public <T> Entity<T> make(Class<T> type) {
		Table table = type.getAnnotation(Table.class);
		if (table == null) {
			throw new DaoException("it's not entity been");
		}
		String tableName = table.value();
		Entity<T> entity = new Entity<T>(type, tableName);
		boolean havePrimary = false;
		List<Field> fields = ReflectionUtils.getDeclaredFields(type);
		for (Field field : fields) {
			EntityField entityField = make(field, type);
			if(entityField == null) continue;
			entity.addEntityField(entityField);
			if(entityField.isId() || entityField.isName()){
				havePrimary = true;
			}
		}
		if(!havePrimary){
			throw new DaoException(" entity [" + type.getSimpleName() + "] have no primary key");
		}
		setNamedQueries(entity);
		return entity;
	}
	
	public <T> void setNamedQueries(Entity<T> entity){
		Class<T> type = entity.getType();
		NamedQueries namedQueries = type.getAnnotation(NamedQueries.class);
		if(namedQueries == null) return;
		NamedQuery[] namedQueryList = namedQueries.value();
		for(NamedQuery namedQuery : namedQueryList){
			String name = namedQuery.name();
			String sql = namedQuery.sql();
			if(StringUtils.isEmpty(name)){
				throw new DaoException(" name is empty for name query with make entity");
			}
			if(StringUtils.isEmpty(sql)){
				throw new DaoException(" sql is empty for name query with make entity");
			}
			entity.putNameQueryMap(namedQuery.name(), namedQuery.sql());
		}
	}

	public EntityField make(Field field, Class<?> type) {
		Column column = field.getAnnotation(Column.class);
		Id id = field.getAnnotation(Id.class);
		Name name = field.getAnnotation(Name.class);
		// 如果都为空 则返回空
		if (column == null && id == null && name == null)
			return null;
		EntityField entityField = new EntityField();
		// fieldName
		String fieldName = field.getName();
		entityField.setName(fieldName);
		// columnName
		if (id != null) {
			entityField.setId(true);
			if(id.auto()) entityField.setAuto(true);
			else entityField.setAuto(false);
		}else if (name != null) {
			entityField.setName(true);
		}
		if (column != null) {
			entityField.setColumnName(column.name());
			entityField.setLength(column.length());
			entityField.setColType(column.type());
			entityField.setPrecision(column.precision());
			entityField.setNotNull(column.notNull());
			entityField.setUnsigned(column.unsigned());
		}
		if (StringUtils.isEmpty(entityField.getColumnName())) {
			entityField.setColumnName(fieldName);
		}
		entityField.setColumnName(entityField.getColumnName().toLowerCase());
		// type
		entityField.setTypeClass(field.getType());
		if (entityField.getColType() == null || ColType.VARCHAR.equals(entityField.getColType())) {
			entityField.setColType(sensitiveType(field.getType()));
		}
		Method readMethod = ReflectionUtils.getReadMethod(fieldName, type);
		if(readMethod == null){
			throw new DaoException("read method not found for field [" + fieldName + "] witdh Class[" + type.getSimpleName() + "]");
		}
		entityField.setReadMethod(readMethod);
		Method writeMethod = ReflectionUtils.getWriteMethod(fieldName, type);
		if(writeMethod == null){
			throw new DaoException("write method not found for field [" + fieldName + "] witdh Class[" + type.getSimpleName() + "]");
		}
		entityField.setWriteMethod(writeMethod);
		return entityField;
	}

	private ColType sensitiveType(Class<?> type) {
		if (Integer.class.equals(type) || int.class.equals(type)) {
			return ColType.INT;
		}
		if (Long.class.equals(type) || long.class.equals(type)) {
			return ColType.LONG;
		}
		if (Float.class.equals(type) || float.class.equals(type) || BigDecimal.class.equals(type)) {
			return ColType.FLOAT;
		}
		if (String.class.equals(type)) {
			return ColType.VARCHAR;
		}
		if (Date.class.equals(type) || Calendar.class.equals(type) || Timestamp.class.equals(type)) {
			return ColType.TIMESTAMP;
		}
		if(java.sql.Date.class.equals(type)){
			return ColType.DATE;
		}
		if(Time.class.equals(type)){
			return ColType.TIME;
		}
		if (Character.class.equals(type)) {
			return ColType.CHAR;
		}
		if (Boolean.class.equals(type)) {
			return ColType.BOOLEAN;
		}
		return ColType.VARCHAR;
	}
}
