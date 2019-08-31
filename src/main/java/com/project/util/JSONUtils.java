package com.project.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.ValueFilter;

/**
 * 
 * {fastJson工具类}
 * 
 * @author zhangxiaota
 * @lastModified
 * @history
 */
public class JSONUtils {

	/**
	 * 
	 * {Object转换为字符串}
	 * 
	 * @param object
	 * @return
	 * @author zhangj
	 * @created 2016年9月22日 上午10:23:04
	 * @lastModified
	 * @history
	 */
	public static String toJson(Object object) {
		return JSONObject.toJSONString(object, filter);
	}
	
	/**
	 * Json数据中有null值,显示并为""
	 */
	private static ValueFilter filter = new ValueFilter() {
		@Override
		public Object process(Object obj, String s, Object v) {
			if (v == null)
				return "";
			return v;
		}
	};
}
