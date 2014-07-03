package com.b5m.dao.exception;

public class DaoException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5190088234814412106L;

	public DaoException(String message){
		super(message);
	}
	
	public DaoException(Throwable e, String message){
		super(message, e);
	}
	
	public DaoException(Throwable e){
		super(e);
	}
}
