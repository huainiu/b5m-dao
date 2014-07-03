package com.b5m.dao;

import java.sql.Connection;

import com.b5m.dao.domain.DaoStatement;
/**
 * @Company B5M.com
 * @description
 * 
 * @author echo
 * @since 2013-7-12
 * @email echo.weng@b5m.com
 */
public interface DaoExecutor {

	void exec(Connection conn, DaoStatement st);

}
