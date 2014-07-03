package com.b5m.dao;

import javax.sql.DataSource;

public interface DaoRunner {
	
	void run(DataSource dataSource, ConnCallback callback);
	
}
