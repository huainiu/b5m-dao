package com.b5m.dao.utils;

import org.apache.log4j.Logger;

public class LogUtils{
    public static void error(Class<?> clazz, Exception e){
    	Logger logger = Logger.getLogger(clazz);
        StackTraceElement[] trace = e.getStackTrace();
        StringBuilder message = new StringBuilder();
        for (int i=0; i < trace.length; i++)
            message.append("\tat " + trace[i]);
        logger.error(clazz.getSimpleName() + " error, error type is [" + e.getClass().getSimpleName() + "], error message[" + e.getMessage() + "], stack trace-->" + message, e);
    }
    
    public static void error(Class<?> clazz, String messge){
    	Logger logger = Logger.getLogger(clazz);
    	logger.error(messge);
    }
    
    public static void info(Class<?> clazz, String messge){
    	Logger logger = Logger.getLogger(clazz);
    	logger.info(messge);
    }
    
    public static void debug(Class<?> clazz, String messge){
    	Logger logger = Logger.getLogger(clazz);
    	logger.debug(messge);
    }
}
