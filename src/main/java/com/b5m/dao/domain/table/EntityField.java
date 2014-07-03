package com.b5m.dao.domain.table;

import java.lang.reflect.Method;

import com.b5m.dao.domain.ColType;
/**
 * @Company B5M.com
 * @description
 * 
 * @author echo
 * @since 2013-7-12
 * @email echo.weng@b5m.com
 */
public class EntityField {
	private Class<?> typeClass;

	private String name;

	private String columnName;

	private ColType colType;

	private int length;
	
	private int precision;//精度

	private boolean readOnly;

	private boolean isId;

	private boolean isName;

	private boolean isAuto = false;
	
	private boolean notNull = false;
	
	private boolean unsigned = true;

	private Method readMethod;

	private Method writeMethod;

	public Class<?> getTypeClass() {
		return typeClass;
	}

	public void setTypeClass(Class<?> typeClass) {
		this.typeClass = typeClass;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public ColType getColType() {
		return colType;
	}

	public void setColType(ColType colType) {
		this.colType = colType;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public boolean isId() {
		return isId;
	}

	public void setId(boolean isId) {
		this.isId = isId;
	}

	public boolean isName() {
		return isName;
	}

	public void setName(boolean isName) {
		this.isName = isName;
	}

	public boolean isAuto() {
		return isAuto;
	}

	public void setAuto(boolean isAuto) {
		this.isAuto = isAuto;
	}

	public Method getReadMethod() {
		return readMethod;
	}

	public void setReadMethod(Method readMethod) {
		this.readMethod = readMethod;
	}

	public Method getWriteMethod() {
		return writeMethod;
	}

	public void setWriteMethod(Method writeMethod) {
		this.writeMethod = writeMethod;
	}

	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public boolean isNotNull() {
		return notNull;
	}

	public void setNotNull(boolean notNull) {
		this.notNull = notNull;
	}

	public boolean isUnsigned() {
		return unsigned;
	}

	public void setUnsigned(boolean unsigned) {
		this.unsigned = unsigned;
	}
	
}
