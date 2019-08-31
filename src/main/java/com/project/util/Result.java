
package com.project.util;


import com.fasterxml.jackson.annotation.JsonInclude;


/**
 * ClassName: Result
 * Function:  TODO  前端返回对象
 * Date:      2019/6/11 22:08
 * author     liuxin
 */

public class Result {

	/***
	 *@Return    返回状态码，为空则默认200.前端需要拦截一些常见的状态码如403、404、500等
	 *@Author liuxin
	 *@Date 2019/6/11
	 *@Time 22:10
	 */
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String status;

	/***
	 *@Return    相关消息
	 *@Author liuxin
	 *@Date 2019/6/11
	 *@Time 22:17
	 */
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String msg;

	/***
	 *@Return 相关数据
	 *@Author liuxin
	 *@Date 2019/6/11
	 *@Time 22:17
	 */
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Object data;

	public Result() {
	}

	public Result(String status, String msg, Object data) {
		this.status = status;
		this.msg = msg;
		this.data = data;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}


