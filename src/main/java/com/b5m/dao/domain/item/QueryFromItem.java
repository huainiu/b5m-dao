package com.b5m.dao.domain.item;

import java.util.List;

import com.b5m.dao.domain.ColType;
import com.b5m.dao.domain.item.SqlItem;
import com.b5m.dao.domain.table.Entity;
/**
 * @Company B5M.com
 * @description
 * 
 * @author echo
 * @since 2013-7-12
 * @email echo.weng@b5m.com
 */
public class QueryFromItem implements SqlItem{
	private int startIndex;
	private int size;
	
	public QueryFromItem(int startIndex, int size){
		this.startIndex = startIndex;
		this.size = size;
	}
	
	public static QueryFromItem newInstance(int currentPage, int pageSize){
		return new QueryFromItem(currentPage, pageSize);
	}
	
	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	@Override
	public void joinSql(Entity<?> en, StringBuilder sb) {
		sb.append(" limit ");
		sb.append(startIndex).append(",").append(size);
	}

	@Override
	public void joinParams(Entity<?> en, List<Object> params,
			List<ColType> colTypes, List<Class<?>> fieldTyps) {
		
	}

}
