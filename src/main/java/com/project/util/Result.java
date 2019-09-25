
package com.project.util;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * ClassName: Result
 * Function:  TODO  前端返回对象
 * Date:      2019/6/11 22:08
 * author     liuxin
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result{

	/***
	 *@Return    返回状态码，为空则默认200.前端需要拦截一些常见的状态码如403、404、500等
	 *@Author liuxin
	 *@Date 2019/6/11
	 *@Time 22:10
	 */
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private int status;

	/***
	 *@Return    相关消息
	 *@Author liuxin
	 *@Date 2019/6/11
	 *@Time 22:17
	 */
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String message;

	/***
	 *@Return 相关数据
	 *@Author liuxin
	 *@Date 2019/6/11
	 *@Time 22:17
	 */
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Object data;

	public Result(int status,String message){
		this.status = status;
		this.message = message;
	}

}


