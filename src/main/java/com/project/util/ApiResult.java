package com.project.util;

/**
 * <p>
 * 接口返回对象
 * </p>
 * 
 * @author hubin
 * @Date 2016-03-15
 */
public class ApiResult {

	/**
	 * 业务结果码
	 */
	private String resultCode;
	/**
	 * 失败或成功的提示信息
	 */
	private String resultMsg;

	/**
	 * 返回的数据
	 */
	private Object result;

	/**
	 * @return the resultCode
	 */
	public String getResultCode() {
		return resultCode;
	}

	/**
	 * @param resultCode
	 *            the resultCode to set
	 */
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	/**
	 * @return the resultMsg
	 */
	public String getResultMsg() {
		return resultMsg;
	}

	/**
	 * @param resultMsg
	 *            the resultMsg to set
	 */
	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	/**
	 * @return the result
	 */
	public Object getResult() {
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(Object result) {
		this.result = result;
	}
}
