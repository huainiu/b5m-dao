package com.b5m.dao;

import java.sql.Connection;

public interface ConnCallback {
	
	void invoke(Connection conn) throws Exception;
	
}
