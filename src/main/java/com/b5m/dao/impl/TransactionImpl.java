package com.b5m.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;

import javax.sql.DataSource;

import com.b5m.dao.domain.Transaction;
/**
 * @Company B5M.com
 * @description
 * 
 * @author echo
 * @since 2013-7-11
 * @email echo.weng@b5m.com
 */
public class TransactionImpl implements Transaction{
	private Savepoint savepoint;
	private Connection connection;

	@Override
	public void commit() {
		try {
			if(connection != null) connection.commit();
		} catch (SQLException e) {
		}
	}

	@Override
	public void rollback() {
		if(connection == null) return;
		try {
			if(savepoint != null)
				connection.rollback(savepoint);
			else
			  connection.rollback();
		} catch (SQLException e) {
		}
	}

	@Override
	public Connection getConnection(DataSource dataSource) throws SQLException {
		if(connection == null) {
			connection = dataSource.getConnection();
			if(connection.getAutoCommit()){
				connection.setAutoCommit(false);
			}
		}
		return connection;
	}

	@Override
	public void close() {
		if(connection == null) return;
		try {
			connection.close();
		} catch (SQLException e) {
		}
	}

	@Override
	public Savepoint getSavepoint() {
		return savepoint;
	}

	@Override
	public void setSavepoint(Savepoint savepoint) {
		this.savepoint = savepoint;
	}

}
