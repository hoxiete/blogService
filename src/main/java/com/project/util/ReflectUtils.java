package com.project.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 
 * {java反射用工具类}
 * 
 * @author zhangxiaota
 * @lastModified
 * @history
 */
public class ReflectUtils {

	/**
	 * set属性的值到Bean
	 * 
	 * @param bean
	 * @param valMap
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	public static void setFieldValue(Object bean, String filedName, String value) {
		try {
			// 根据属性名称获取Field
			Field name = bean.getClass().getDeclaredField(filedName);
			// 设置为可写
			name.setAccessible(true);
			// 设置
			name.set(bean, value);
			// 设置为不可写
			name.setAccessible(false);
		} catch (Exception e) {
			return;
		}
	}

	/**
	 * set属性的值到Bean
	 * 
	 * @param bean
	 * @param valMap
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	@SuppressWarnings("finally")
	public static String getFieldValue(Object bean, String filedName) {
		String value = "";
		try {
			// 将属性的首字符大写，方便构造get，set方法
			filedName = filedName.substring(0, 1).toUpperCase() + filedName.substring(1);
			Method m = bean.getClass().getMethod("get" + filedName);
			// 调用getter方法获取属性值
			value = (String) m.invoke(bean);
		} catch (Exception e) {
			value = "";
		} finally {
			return value;
		}
	}
}
