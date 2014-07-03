package com.b5m.dao.domain.page;

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
public class PageCnd implements SqlItem{
	private int currentPage;
	private int pageSize;
	
	public PageCnd(int currentPage, int pageSize){
		this.currentPage = currentPage;
		this.pageSize = pageSize;
	}
	
	public static PageCnd newInstance(int currentPage, int pageSize){
		return new PageCnd(currentPage, pageSize);
	}
	
	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	@Override
	public void joinSql(Entity<?> en, StringBuilder sb) {
		sb.append(" limit ");
		sb.append(pageSize * (currentPage - 1)).append(",").append(pageSize);
	}

	@Override
	public void joinParams(Entity<?> en, List<Object> params,
			List<ColType> colTypes, List<Class<?>> fieldTyps) {
		
	}

}
