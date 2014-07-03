package com.b5m.dao.domain.orderby;

/**
 * @Company B5M.com
 * @description
 * 
 * @author echo
 * @since 2013-7-12
 * @email echo.weng@b5m.com
 */
public class OrderByItem {
	private String name;

	private boolean isDesc;

	private OrderByItem() {
	};

	private OrderByItem(String name, boolean isDesc) {
		this.name = name;
		this.isDesc = isDesc;
	};

	public String getName() {
		return name;
	}

	public boolean isDesc() {
		return isDesc;
	}

	public static OrderByItem asc(String name) {
		return new OrderByItem(name, false);
	}

	public static OrderByItem desc(String name) {
		return new OrderByItem(name, true);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isDesc ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		OrderByItem other = (OrderByItem) obj;
		if (isDesc != other.isDesc)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}