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
public class PageView<T> {
	//总记录
	private List<T> records;
	private PageIndex pageIndex;
	private long totalPage = 1;//总页数
	private int pageSize = 10;//每页最多显示几条
	private int currentPage = 1;//当前页
	private long totalRecord;//总记录数
	private int pageCode = 5;//在界面需要显示几个页码
	/**
	 * 例子：/search/s/_____________{pageCode}______毛衣.html
	 */
	private String url;//模版路径 {pageCode} 表示站位符 

	public int getPageCode() {
		return pageCode;
	}

	public void setPageCode(int pageCode) {
		this.pageCode = pageCode;
	}

	public PageView(int pageSize, int currentPage) {
		this.pageSize = pageSize;
		this.currentPage = currentPage;
	}

	public void setQueryResult(QueryResult<T> qr) {
		setTotalRecord(qr.getTotalRecord());
		setRecords(qr.getResultList());
	}

	public long getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(long totalRecord) {
		this.totalRecord = totalRecord;
		setTotalPage(this.totalRecord % this.pageSize == 0 ? this.totalRecord / this.pageSize : this.totalRecord
				/ this.pageSize + 1);
	}

	public List<T> getRecords() {
		return records;
	}

	public void setRecords(List<T> records) {
		this.records = records;
	}

	public PageIndex getPageIndex() {
		return pageIndex;
	}

	public long getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(long totalPage) {
		this.totalPage = totalPage;
		this.pageIndex = PageIndex.getPageIndex(pageCode, currentPage, totalPage);
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + currentPage;
		result = prime * result + pageCode;
		result = prime * result
				+ ((pageIndex == null) ? 0 : pageIndex.hashCode());
		result = prime * result + pageSize;
		result = prime * result + ((records == null) ? 0 : records.hashCode());
		result = prime * result + (int) (totalPage ^ (totalPage >>> 32));
		result = prime * result + (int) (totalRecord ^ (totalRecord >>> 32));
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
		PageView other = (PageView) obj;
		if (currentPage != other.currentPage)
			return false;
		if (pageCode != other.pageCode)
			return false;
		if (pageIndex == null) {
			if (other.pageIndex != null)
				return false;
		} else if (!pageIndex.equals(other.pageIndex))
			return false;
		if (pageSize != other.pageSize)
			return false;
		if (records == null) {
			if (other.records != null)
				return false;
		} else if (!records.equals(other.records))
			return false;
		if (totalPage != other.totalPage)
			return false;
		if (totalRecord != other.totalRecord)
			return false;
		return true;
	}

}
