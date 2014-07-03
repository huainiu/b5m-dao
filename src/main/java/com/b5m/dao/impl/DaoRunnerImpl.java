package com.b5m.dao.impl;

import java.sql.Connection;
import java.sql.Savepoint;

import javax.sql.DataSource;

import com.b5m.dao.ConnCallback;
import com.b5m.dao.DaoRunner;
import com.b5m.dao.Trans;
import com.b5m.dao.domain.Transaction;
import com.b5m.dao.exception.DaoException;
import com.b5m.dao.utils.LogUtils;
/**
 * @Company B5M.com
 * @description
 * 
 * @author echo
 * @since 2013-7-10
 * @email echo.weng@b5m.com
 */
public class DaoRunnerImpl implements DaoRunner {

	public void run(DataSource dataSource, ConnCallback callback) {
		try {
			Transaction transaction = Trans.get();
			if(transaction != null){
				Connection conn = null;
				try {
					conn = transaction.getConnection(dataSource);
					Savepoint savepoint = transaction.getSavepoint();
					if(savepoint == null) {
						transaction.setSavepoint(conn.setSavepoint());
					}
					callback.invoke(conn);
				} catch (Exception e) {
					LogUtils.error(this.getClass(), e);
					if(conn != null){
						LogUtils.error(this.getClass(), "transaction rollback");
						conn.rollback(transaction.getSavepoint());
					}
					throw new DaoException(e);
				} 
			}else{//无事务
				Connection conn = null;
				try {
					conn = dataSource.getConnection();
					conn.setAutoCommit(false);
					callback.invoke(conn);
					if(!conn.getAutoCommit()){
						conn.commit();
					}
				} catch (Exception e) {
					LogUtils.error(this.getClass(), e);
					try {
						conn.rollback();
					} catch (Exception e1) {
					}
					throw new DaoException(e, e.getMessage());
				}finally{
					if (null != conn) {
						conn.close();						
					}
				}
			}
		} catch (Exception e) {
			LogUtils.error(this.getClass(), e);
			throw new DaoException(e, e.getMessage());
		}
		
	}
}
