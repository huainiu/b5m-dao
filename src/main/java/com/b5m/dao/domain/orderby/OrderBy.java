package com.b5m.dao.domain.orderby;
/**
 * @Company B5M.com
 * @description
 * 
 * @author echo
 * @since 2013-7-12
 * @email echo.weng@b5m.com
 */
public interface OrderBy {
	
	OrderBy asc(String name);

	OrderBy desc(String name);
}
