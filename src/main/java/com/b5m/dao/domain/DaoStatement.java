package com.b5m.dao.domain;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.b5m.dao.domain.item.SqlItem;
import com.b5m.dao.domain.page.PageView;
import com.b5m.dao.domain.table.Entity;
import com.b5m.dao.domain.table.EntityField;
import com.b5m.dao.exception.DaoException;
import com.b5m.dao.utils.ReflectionUtils;
/**
 * @Company B5M.com
 * @description
 * 组装sql 和 参数的核心对象
 * @author echo
 * @since 2013-7-12
 * @email echo.weng@b5m.com
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DaoStatement {
	private Entity<?> entity;
	
	private List<Object> params;
	
	private int updateCount;
	
	private List<ColType> colTypes;
	
	private List<Class<?>> fieldTypes;
	
	private int startIndex = -1;
	
	private int pageSize = -1;
	
	private int totalCount;
	
	private SqlType sqlType;
	
	private List<SqlItem> items;
	
	private Object operationObject;
	
	private Object result;
	
	private PageView<?> pageView;
	
	/**
	 * 现在只被queryName这个方法使用
	 */
	private Class<?> rtnType;
	
	public DaoStatement(SqlType sqlType, Entity<?> entity){
		items = new ArrayList<SqlItem>(10);
		this.sqlType = sqlType;
		this.entity = entity;
	}
	
	public Entity<?> getEntity() {
		return entity;
	}

	public void setEntity(Entity<?> entity) {
		this.entity = entity;
	}

	public List<Object> getParams() {
		return params;
	}

	public void setParams(List<Object> params) {
		this.params = params;
	}

	public int getUpdateCount() {
		return updateCount;
	}

	public void setUpdateCount(int updateCount) {
		this.updateCount = updateCount;
	}

	public List<ColType> getColTypes() {
		return colTypes;
	}

	public void setColTypes(List<ColType> colTypes) {
		this.colTypes = colTypes;
	}

	public void setResult(List<?> result) {
		this.result = result;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public SqlType getSqlType() {
		return sqlType;
	}

	public void setSqlType(SqlType sqlType) {
		this.sqlType = sqlType;
	}

	public void addItem(SqlItem item){
		items.add(item);
	}
	
	public List<SqlItem> getItem(){
		return items;
	}
	
	public boolean isPrimaryAuto(){
		return entity.getIdField() != null && entity.getIdField().isAuto();
	}
	
	public String toSql(){
		StringBuilder sb = new StringBuilder();
		params = new ArrayList<Object>();
		colTypes = new ArrayList<ColType>();
		fieldTypes = new ArrayList<Class<?>>();
		for(SqlItem item : items){
			item.joinSql(this.entity, sb);
			sb.append(" ");
			item.joinParams(this.entity, params, colTypes, fieldTypes);
		}
		return sb.toString();
	}
	
	public List<Class<?>> getFieldTypes() {
		return fieldTypes;
	}

	public void setFieldTypes(List<Class<?>> fieldTypes) {
		this.fieldTypes = fieldTypes;
	}
	
	public Object getOperationObject() {
		return operationObject;
	}

	public void setOperationObject(Object operationObject) {
		this.operationObject = operationObject;
	}
	
	public <T> List<T> getList(Class<T> clazz){
		if(!(result instanceof List)){
			List<T> list = new ArrayList<T>();
			list.add((T)result);
			return list;
		}
		return (List<T>) result;
	}
	
	public <T> T getObject(Class<T> clazz){
		if(result == null) return null;
		if(result instanceof List){
			List list = (List) result;
			if(list.size() < 1) return null;
			return (T) list.get(0);
		}
		return (T) result;
	}
	
	public Object getObject(){
		return result;
	}
	
	public <T> PageView<T> getPageView(Class<T> clazz){
		return (PageView<T>) pageView;
	}
	
	public Class<?> getRtnType() {
		return rtnType;
	}

	public void setRtnType(Class<?> rtnType) {
		this.rtnType = rtnType;
	}

	public void onBefore(Connection conn) throws SQLException{
		
	}
	
	public void onAfter(Connection conn, ResultSet rs) throws SQLException{
		if(rs == null) return;
		if(SqlType.ADD.equals(sqlType) && rs.next()){
			dealWithAdd(rs);
			return;
		}
		if(SqlType.SELECTCOUNT.equals(sqlType)){
			dealWithSelectCount(rs);
			return;
		}
		if(SqlType.CREATEENTITY.equals(sqlType)){
			dealWithCreateEntity(rs);
			return;
		}
		List list = rtnList(rs);
		result = list;
	}
	
	private void dealWithCreateEntity(ResultSet rs) throws SQLException{
		Map<String, Integer> map = new HashMap<String, Integer>();
		ResultSetMetaData resultSetMetaData = rs.getMetaData();
		int length = resultSetMetaData.getColumnCount();
		for(int index = 0; index < length; index++){
			map.put(resultSetMetaData.getColumnLabel(index + 1), resultSetMetaData.getColumnType(index + 1));
		}
		result = map;
	}
	
	private void dealWithSelectCount(ResultSet rs) throws SQLException{
		if(rs.next()){
			result = rs.getInt(1);
		}
	}
	
	private void dealWithAdd(ResultSet rs){
		EntityField field = entity.getIdField();
		try {
			field.getWriteMethod().invoke(operationObject, rs.getLong(1));
		} catch (Exception e) {
			throw new DaoException("error message is " + e.getMessage());
		}
		result = operationObject;
	}
	
	private List rtnList(ResultSet rs) throws SQLException{
		if(rtnType != null) return rtnList(rs, rtnType);
		List list = new ArrayList();
		Class<?> type = entity.getType();
		while(rs.next()){
			Object o = createObject(type);
			list.add(o);
			ResultSetMetaData resultSetMetaData = rs.getMetaData();
			int length = resultSetMetaData.getColumnCount();
			for(int index = 0; index < length; index++){
				String columnName = resultSetMetaData.getColumnLabel(index + 1);
				EntityField field = entity.getColumn(columnName.toLowerCase());
				if(field == null){
					throw new DaoException("set value error with EntityField no found for columnName[" + columnName + "],error message is EntityField null");
				}
				setValue(field, o, rs);
			}
		}
		return list;
	}
	
	/**
	 * @description
	 * 如果rtnType不为空，则说明这个查询是从queryName中来的, 而且传递过来的不是一个实体类
	 * @param rs
	 * @param rtnType
	 * @return
	 * @return List
	 * @date 2013-7-15
	 * @author xiuqing.weng
	 * @throws SQLException 
	 */
	private List rtnList(ResultSet rs, Class<?> rtnType) throws SQLException{
		List list = new ArrayList();
		Map<String, Field> methodMap = rtnRtnTypeMethodMap(rtnType);
		while(rs.next()){
			Object value = returnBaseTypeValue(rs, rtnType);
			if(value != null){
				list.add(value);
				continue;
			}
			Object o = null;
			boolean isMap = ReflectionUtils.isSuperClass(rtnType, Map.class);
			if(isMap){
				o = new HashMap();
			}else{
				o = createObject(rtnType);
			}
			list.add(o);
			ResultSetMetaData resultSetMetaData = rs.getMetaData();
			int length = resultSetMetaData.getColumnCount();
			for(int index = 0; index < length; index++){
				String columnName = resultSetMetaData.getColumnLabel(index + 1);
				if(isMap){
					((Map)o).put(columnName, rs.getString(columnName));
					continue;
				}
				Field field = methodMap.get(columnName.toLowerCase().replace("_", ""));
				if(field == null) continue;
				EntityField fakeField = new EntityField();
				Method method = ReflectionUtils.getWriteMethod(field.getName(), rtnType);
				if(method == null){
					throw new DaoException("write method no found for clazz type ["+ rtnType + "], property[" + field.getName() + "]");
				}
				fakeField.setWriteMethod(method);
				fakeField.setTypeClass(field.getType());
				fakeField.setColumnName(columnName);
				setValue(fakeField, o, rs);
			}
		}
		return list;
	}
	
	/**
	 * 为基础类型设值
	 * @throws SQLException 
	 */
	public Object returnBaseTypeValue(ResultSet rs, Class<?> rtnType) throws SQLException{
		Class<?> fieldType = rtnType;
		if(char.class.equals(fieldType) || Character.class.equals(fieldType)){
			return rs.getString(1);
		}else if(String.class.equals(fieldType)){
			return rs.getString(1);
		}else if(Integer.class.equals(fieldType) || int.class.equals(fieldType)){
			return rs.getInt(1);
		}else if(BigDecimal.class.equals(fieldType)){
			return rs.getBigDecimal(1);
		}else if(Boolean.class.equals(fieldType) || boolean.class.equals(fieldType)){
			return rs.getBoolean(1);
		}else if(Long.class.equals(fieldType) || long.class.equals(fieldType)){
			return rs.getLong(1);
		}else if(Byte.class.equals(fieldType) || byte.class.equals(fieldType)){
			return rs.getByte(1);
		}else if(Short.class.equals(fieldType) || short.class.equals(fieldType)){
			return rs.getShort(1);
		}else if(Float.class.equals(fieldType) || float.class.equals(fieldType)){
			return rs.getFloat(1);
		}else if(Double.class.equals(fieldType) || double.class.equals(fieldType)){
			return rs.getDouble(1);
		}else if(Date.class.equals(fieldType) || Timestamp.class.equals(fieldType) || Time.class.equals(fieldType) || java.sql.Date.class.equals(fieldType)){			
			return new Date(rs.getTimestamp(1).getTime());
		}else if(Number.class.equals(fieldType)){
			return rs.getInt(1);
		}else{
			return null;
		}
	}
	
	Map<String, Field> rtnRtnTypeMethodMap(Class<?> rtnType){
		List<Field> fields = ReflectionUtils.getDeclaredFields(rtnType);
		Map<String, Field> writeMethodMap = new HashMap<String, Field>();
		for(Field field : fields){
			writeMethodMap.put(field.getName().toLowerCase(), field);
		}
		return writeMethodMap;
	}
	
	private Object createObject(Class<?> type) {
		try {
			return type.newInstance();
		} catch (InstantiationException e) {
			throw new DaoException("create object error with type is [" + type.getSimpleName() + "], error message is " + e.getMessage());
		} catch (IllegalAccessException e) {
			throw new DaoException("create object error with type is [" + type.getSimpleName() + "], error message is " + e.getMessage());
		}
	}
	
	private void setValue(EntityField field, Object o, ResultSet rs){
		try {
			Class<?> fieldType = field.getTypeClass();
			String colName = field.getColumnName();
			if(char.class.equals(fieldType) || Character.class.equals(fieldType)){
				field.getWriteMethod().invoke(o, rs.getString(colName));
			}else if(String.class.equals(fieldType)){
				field.getWriteMethod().invoke(o, rs.getString(colName));
			}else if(Integer.class.equals(fieldType) || int.class.equals(fieldType)){
				field.getWriteMethod().invoke(o, rs.getInt(colName));
			}else if(BigDecimal.class.equals(fieldType)){
				field.getWriteMethod().invoke(o, rs.getBigDecimal(colName));
			}else if(Boolean.class.equals(fieldType) || boolean.class.equals(fieldType)){
				field.getWriteMethod().invoke(o, rs.getBoolean(colName));
			}else if(Long.class.equals(fieldType) || long.class.equals(fieldType)){
				field.getWriteMethod().invoke(o, rs.getLong(colName));
			}else if(Byte.class.equals(fieldType) || byte.class.equals(fieldType)){
				field.getWriteMethod().invoke(o, rs.getByte(colName));
			}else if(Short.class.equals(fieldType) || short.class.equals(fieldType)){
				field.getWriteMethod().invoke(o, rs.getShort(colName));
			}else if(Float.class.equals(fieldType) || float.class.equals(fieldType)){
				field.getWriteMethod().invoke(o, rs.getFloat(colName));
			}else if(Double.class.equals(fieldType) || double.class.equals(fieldType)){
				field.getWriteMethod().invoke(o, rs.getDouble(colName));
			}else if(Date.class.equals(fieldType) || Timestamp.class.equals(fieldType) || Time.class.equals(fieldType) || java.sql.Date.class.equals(fieldType)){			
				field.getWriteMethod().invoke(o, rs.getTimestamp(colName));
			}else if(Number.class.equals(fieldType)){
				field.getWriteMethod().invoke(o, rs.getInt(colName));
			}else{
				field.getWriteMethod().invoke(o, rs.getString(colName));
			}
		} catch (Exception e) {
			throw new DaoException("set value for field[" + field.getName() + "] error, error message is " + e.getMessage());
		}
	}
	
}
