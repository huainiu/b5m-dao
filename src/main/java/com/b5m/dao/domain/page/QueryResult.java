package com.b5m.dao.domain.page;

import java.util.List;
/**
 * @Company B5M.com
 * @description
 * 
 * @author echo
 * @since 2013-7-12
 * @email echo.weng@b5m.com
 */
public class QueryResult<T> {
	private List<T> resultList;
	private long totalRecord;

	public QueryResult(){}
	
	public QueryResult(List<T> resultList, long totalRecord){
		this.resultList = resultList;
		this.totalRecord = totalRecord;
	}
	
	public static <T> QueryResult<T> newInstance(List<T> resultList, long totalRecord){
		return new QueryResult<T>(resultList, totalRecord);
	}
	
	public List<T> getResultList() {
		return resultList;
	}

	public void setResultList(List<T> resultList) {
		this.resultList = resultList;
	}

	public long getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(long totalRecord) {
		this.totalRecord = totalRecord;
	}

}
