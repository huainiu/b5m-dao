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
public class Static implements ISqlExp{
	private String str;
	
	public Static(String str){
		this.str = str;
	}
	
	@Override
	public String toString() {
		return " " + str + " ";
	}

	@Override
	public void joinSql(Entity<?> en, StringBuilder sb) {
		sb.append(' ').append(str).append(' ');
	}

	@Override
	public void joinParams(Entity<?> en, List<Object> params, List<ColType> colTypes, List<Class<?>> fieldTypes) {
		//无参数操作
		return;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((str == null) ? 0 : str.hashCode());
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
		Static other = (Static) obj;
		if (str == null) {
			if (other.str != null)
				return false;
		} else if (!str.equals(other.str))
			return false;
		return true;
	}
	
}
