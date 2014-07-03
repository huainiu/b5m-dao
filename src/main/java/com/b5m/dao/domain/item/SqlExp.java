package com.b5m.dao.domain.item;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.b5m.dao.domain.ColType;
import com.b5m.dao.domain.cnd.Op;
import com.b5m.dao.domain.table.Entity;
import com.b5m.dao.domain.table.EntityField;
import com.b5m.dao.exception.DaoException;
import com.b5m.dao.exception.SqlExpsCheckValueException;
/**
 * @Company B5M.com
 * @description
 * 
 * @author echo
 * @since 2013-7-10
 * @email echo.weng@b5m.com
 */
public class SqlExp implements ISqlExp{
	private final String name;
	private final Op op;
	private final Object value;

	public static SqlExp newInstance(String name, Op op, Object value){
		return new SqlExp(name, op, value);
	}
	
	private SqlExp(String name, Op op, Object value){
		this.name = name;
		this.op = op;
		if(value instanceof List){
			List<?> list = (List<?>) value;
			this.value = list.toArray();
		}else{
			this.value = value;
		}
		checkValue();
	}
	
	public String getName() {
		return name;
	}

	public Op getOp() {
		return op;
	}

	public Object getValue() {
		return value;
	}
	
	public void checkValue(){
		if(Op.BETWEEN.equals(op)){
			if(!(this.value instanceof Object[])){
				throw new SqlExpsCheckValueException(" illegal value with op is between ");
			}
			Object[] vs = (Object[]) this.value;
			if(vs.length != 2){
				throw new SqlExpsCheckValueException(" illegal value with op is between ");
			}
		}
		if(Op.IN.equals(op)){
			if(!(this.value instanceof Object[])){
				throw new SqlExpsCheckValueException(" illegal value with op is in ");
			}
		}
	}

	@Override
	public String toString() {
		if(Op.BETWEEN.equals(op)){
			Object[] vs = (Object[]) value;
			return name + op + vs[0] + " and " + vs[1];
		}
		return name + op + value;
	}

	@Override
	public void joinSql(Entity<?> en, StringBuilder sb) {
		if(!(Op.ISNOTNULL.equals(op) || Op.ISNULL.equals(op)) && value == null) return;
		EntityField field = en.getField(name);
		if(field == null) {
			throw new DaoException(" field [" + name + "] for SqlExp width operation is[" + op + "] is not found for Entity[" + en.getType().getSimpleName() + "]");
		}
		sb.append(" ").append(field.getColumnName()).append(" ");
		if(Op.BETWEEN.equals(op)){//between ?,?
			sb.append("between").append(" ").append(" ? AND ? ");
		}else if(Op.ISNOTNULL.equals(op) || Op.ISNULL.equals(op)){
			sb.append(op).append(" ");
		}else if(Op.IN.equals(op)){
			Object[] values = (Object[]) value;
			sb.append(op).append("(");
			int length = values.length;
			for(int index = 0; index < length; index++){
				sb.append("?");
				if(index < length - 1){
					sb.append(",");
				}
			}
			sb.append(")");
		}else{
			sb.append(op).append(" ").append(" ? ");
		}
	}

	@Override
	public void joinParams(Entity<?> en, List<Object> params, List<ColType> colTypes, List<Class<?>> fieldTypes) {
		if(value == null) return;
		if(StringUtils.isEmpty(value.toString().trim())) return;
		EntityField field = en.getField(name);
		if(field == null) {
			throw new DaoException(" field [" + name + "] for SqlExp width operation is[" + op + "] is not found for Entity[" + en.getType().getSimpleName() + "]");
		}
		if(Op.LIKE.equals(op)){
			params.add("%" + value + "%");
		}else if(Op.IN.equals(op) || Op.BETWEEN.equals(op)){
			Object[] values = (Object[]) value;
			int length = values.length;
			for(int index = 0; index < length; index++){
				params.add(values[index]);
				colTypes.add(field.getColType());
				fieldTypes.add(field.getTypeClass());
			}
			return;
		}else{
			params.add(value);
		}
		colTypes.add(field.getColType());
		fieldTypes.add(field.getTypeClass());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((op == null) ? 0 : op.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SqlExp other = (SqlExp) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (op != other.op)
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
}
