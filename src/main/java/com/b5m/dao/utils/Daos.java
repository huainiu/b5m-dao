package com.b5m.dao.utils;

import java.sql.ResultSet;
import java.sql.Statement;
/**
 * @Company B5M.com
 * @description
 * 
 * @author echo
 * @since 2013-7-12
 * @email echo.weng@b5m.com
 */
public class Daos {
	public static void safeClose(Statement statement, ResultSet rs) {
		safeClose(rs);
		safeClose(statement);
	}

	public static void safeClose(Statement statement) {
		if (null != statement)
			try {
				statement.close();
			} catch (Throwable e) {
			}
	}

	public static void safeClose(ResultSet rs) {
		if (null != rs)
			try {
				rs.close();
			} catch (Throwable e) {
			}
	}

}
