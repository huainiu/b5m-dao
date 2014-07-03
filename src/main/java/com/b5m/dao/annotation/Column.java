package com.b5m.dao.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.b5m.dao.domain.ColType;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface Column {
	
	String name() default "";
	
	ColType type() default ColType.VARCHAR;
	
	int length() default 0;
	
	boolean notNull() default false;
    
	int precision() default 2;
	
	boolean unsigned() default true;
}
