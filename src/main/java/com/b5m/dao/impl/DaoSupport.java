package com.b5m.dao.impl;

import java.sql.Connection;

import javax.sql.DataSource;

import com.b5m.dao.ConnCallback;
import com.b5m.dao.DaoExecutor;
import com.b5m.dao.DaoRunner;
import com.b5m.dao.EntityMaker;
import com.b5m.dao.domain.DaoStatement;
/**
 * @Company B5M.com
 * @description
 * 
 * @author echo
 * @since 2013-7-10
 * @email echo.weng@b5m.com
 */
public class DaoSupport {
	protected DataSource dataSource;
	
	/**
	 * Entity 获取对象
	 */
	protected EntityHolder holder;
	
	protected DaoRunner runner;
	
	protected DaoExecutor executor;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		holder = new EntityHolder(createEntityMaker());
	}
	
	public void setRunner(DaoRunner runner) {
		this.runner = runner;
	}
	
	public void setExecutor(DaoExecutor executor) {
		this.executor = executor;
	}

	protected EntityMaker createEntityMaker(){
		return new EntityMakerImpl();
	}
	
	public int _exec(final DaoStatement... sts) {
		final int[] re = new int[1];
		runner.run(dataSource, new ConnCallback() {
			public void invoke(Connection conn) throws Exception {
				for (DaoStatement st : sts) {
					executor.exec(conn, st);
					re[0] += st.getUpdateCount();
				}
			}
		});
		return re[0];
	}
}
