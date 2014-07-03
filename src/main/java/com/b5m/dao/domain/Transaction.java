package com.b5m.dao.domain;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;

import javax.sql.DataSource;

public interface Transaction {
	void commit();

	void rollback();

	Connection getConnection(DataSource dataSource) throws SQLException;

	void close();
	
	Savepoint getSavepoint();
	
	void setSavepoint(Savepoint savepoint);
}
