package com.b5m.dao.utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
/**
 * @author echo
 */
public class ReflectionUtils {
	/**
	 * @description
	 * 获取writeMethod方法，获取不到，则返回为空
	 * @param fieldName
	 * @param clazz
	 * @return
	 * @return Method
	 * @date 2013-7-15
	 * @author xiuqing.weng
	 */
	public static Method getWriteMethod(String propertyName, Class<?> clazz){
		try {
			PropertyDescriptor descriptor = new PropertyDescriptor(propertyName, clazz);
			return descriptor.getWriteMethod();
		} catch (IntrospectionException e) {
			return null;
		}
	}
	
	/**
	 * @description
	 * 获取read方法，获取不到则返回为空
	 * @param propertyName
	 * @param clazz
	 * @return
	 * @return Method
	 * @date 2013-7-15
	 * @author xiuqing.weng
	 */
	public static Method getReadMethod(String propertyName, Class<?> clazz){
		try {
			PropertyDescriptor descriptor = new PropertyDescriptor(propertyName, clazz);
			return descriptor.getReadMethod();
		} catch (IntrospectionException e) {
			return null;
		}
	}
	
	/**
	 * @description
	 * 获取clazz的所有Field 包括父类的
	 * @param clazz
	 * @return
	 * @return List<Field>
	 * @date 2013-7-15
	 * @author xiuqing.weng
	 */
	public static List<Field> getDeclaredFields(Class<?> clazz){
		List<Field> fields = new ArrayList<Field>();
		if(Object.class.equals(clazz) || clazz == null) return fields;
		fields.addAll(getDeclaredFields(clazz.getSuperclass()));
		Field[] fieldArray = clazz.getDeclaredFields();
		for(Field field : fieldArray){
			fields.add(field);
		}
		return fields;
	}
	
	public static boolean isClass(Object o, Class<?> cls) {
		return isSuperClass(o.getClass(), cls);
	}

	public static boolean isSuperClass(Class<?> cls, Class<?> superCls) {
		if (superCls.equals(Object.class) || cls == superCls)
			return true;
		if (superCls.isInterface()) {
			Class<?>[] clss = cls.getInterfaces();
			for (Class<?> c : clss)
				if (c.equals(superCls))
					return true;
		}
		Class<?> pcls = cls.getSuperclass();
		while (!pcls.equals(Object.class)) {
			if (pcls.equals(superCls))
				return true;
			pcls = pcls.getSuperclass();
		}
		return false;
	}
	
}
