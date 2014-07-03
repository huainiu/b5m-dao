package com.b5m.dao;

import com.b5m.dao.domain.table.Entity;


public interface EntityMaker {
	
	/**
	 * 根据Type 生成Entity
	 */
	<T> Entity<T> make(Class<T> type);
}
