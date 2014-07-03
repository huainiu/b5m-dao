package com.b5m.dao.domain.page;
/**
 * @Company B5M.com
 * @description
 * 
 * @author echo
 * @since 2013-7-12
 * @email echo.weng@b5m.com
 */
public class PageIndex {
	private long startindex;
	private long endindex;
	
	public PageIndex(long startindex, long endindex) {
		this.startindex = startindex;
		this.endindex = endindex;
	}
	
	public long getStartindex() {
		return startindex;
	}
	
	public void setStartindex(long startindex) {
		this.startindex = startindex;
	}
	
	public long getEndindex() {
		return endindex;
	}
	
	public void setEndindex(long endindex) {
		this.endindex = endindex;
	}
	 
	public static PageIndex getPageIndex(long viewpagecount, int currentPage, long totalpage){
		long startpage = currentPage-(viewpagecount%2==0 ? viewpagecount/2-1 : viewpagecount/2);
		long endpage = currentPage+viewpagecount/2;
		if(startpage<1){
			startpage = 1;
			if(totalpage>=viewpagecount) endpage = viewpagecount;
			else endpage = totalpage;
		}
		if(endpage>totalpage){
			endpage = totalpage;
			if((endpage-viewpagecount)>0) startpage = endpage-viewpagecount+1;
			else startpage = 1;
		}
		return new PageIndex(startpage, endpage);		
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (endindex ^ (endindex >>> 32));
		result = prime * result + (int) (startindex ^ (startindex >>> 32));
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
		PageIndex other = (PageIndex) obj;
		if (endindex != other.endindex)
			return false;
		if (startindex != other.startindex)
			return false;
		return true;
	}
	
}
