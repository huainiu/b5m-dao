package com.b5m.dao;

import com.b5m.dao.domain.Transaction;
import com.b5m.dao.exception.DaoException;
import com.b5m.dao.impl.TransactionImpl;
import com.b5m.dao.utils.LogUtils;
/**
 * @Company B5M.com
 * @description
 * 
 * @author echo
 * @since 2013-7-11
 * @email echo.weng@b5m.com
 */
public class Trans {
	static ThreadLocal<Transaction> tranThreadLocal = new ThreadLocal<Transaction>();
	
	public static Transaction get(){
		return tranThreadLocal.get();
	}
	
	public static void begin(){
		Transaction transaction = get();
		if(transaction == null){
			transaction = new TransactionImpl();
			tranThreadLocal.set(transaction);
		}
	}

	public static void commit(){
		Transaction transaction = get();
		if(transaction != null) transaction.commit();
	}
	
	public static void close(){
		Transaction transaction = get();
		transaction.close();
		tranThreadLocal.set(null);
	}
	
	public static void rollback(){
		Transaction transaction = tranThreadLocal.get();
		if(transaction != null){
			transaction.rollback();
		}
	}
	
	public static void _exe(Runnable ...runners){
		try {
			LogUtils.info(Trans.class, "transaction starting...");
			begin();
			for(Runnable runner : runners){
				runner.run();
			}
			commit();
			LogUtils.info(Trans.class, "transaction commit...");
			close();
		} catch (Exception e) {
			LogUtils.error(Trans.class, e);
			LogUtils.error(Trans.class, "transaction rollback...");
			rollback();
			throw new DaoException(e);
		}
	}
}
