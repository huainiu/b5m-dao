package com.b5m.dao.domain.orderby;

import java.util.ArrayList;
import java.util.List;

import com.b5m.dao.domain.ColType;
import com.b5m.dao.domain.item.SqlItem;
import com.b5m.dao.domain.table.Entity;
import com.b5m.dao.domain.table.EntityField;
import com.b5m.dao.exception.DaoException;
/**
 * @Company B5M.com
 * @description
 * 
 * @author echo
 * @since 2013-7-12
 * @email echo.weng@b5m.com
 */
public class OrderBySet implements OrderBy, SqlItem{
	private List<OrderByItem> list;

	public OrderBySet(){
		list = new ArrayList<OrderByItem>(3);
	}
	
	@Override
	public OrderBy asc(String name) {
		list.add(OrderByItem.asc(name));
		return this;
	}

	@Override
	public OrderBy desc(String name) {
		list.add(OrderByItem.desc(name));
		return this;
	}

	@Override
	public void joinSql(Entity<?> en, StringBuilder sb) {
		int length = list.size();
		if(length <= 0){
			return;
		}
		sb.append("ORDER BY").append(" ");
		for(int index = 0; index < length; index++){
			OrderByItem orderByItem = list.get(index);
			EntityField entityField = en.getField(orderByItem.getName());
			if(entityField == null){
				throw new DaoException(" field [" + orderByItem.getName() + "] for order by is not found for Entity[" + en.getType().getSimpleName() + "]");
			}
			boolean isDesc = orderByItem.isDesc();
			sb.append(entityField.getColumnName()).append(" ");
			if(isDesc) sb.append("DESC");
			else sb.append("ASC");
			if(index < length - 1) sb.append(",");
		}
	}

	@Override
	public void joinParams(Entity<?> en, List<Object> params, List<ColType> colTypes, List<Class<?>> fieldTyps) {
		//不涉及参数操作
		return;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((list == null) ? 0 : list.hashCode());
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
		OrderBySet other = (OrderBySet) obj;
		if (list == null) {
			if (other.list != null)
				return false;
		} else if (!list.equals(other.list))
			return false;
		return true;
	}

}
