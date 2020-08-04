package com.project.util;

/**
 * ClassName: ResultMsg
 * Function:  TODO  返回结果描述
 * Date:      2019/6/27 13:46
 * author     liuxin
 */
public class ResultMsg {
    /**
     * 请求已经成功处理
     */
    public static final String OK = "请求已经成功处理";

    /**
     * 请求错误，请修正请求
     */
    public static final String BAD_REQUEST = "请求错误，请修正请求";
    /**
     * 请求错误，用户被锁定
     */
    public static final String USER_LOCKED = "登录失败，已被锁定";

    /**
     * Token验证失败
     */
    public static final String SC_UNAUTHORIZED = "Token验证失败";

    /**
     * 资源未找到
     */
    public static final String NOT_FOUND = "资源未找到";

    /**
     * 服务器内部错误
     */
    public static final String INTERNAL_SERVER_ERROR = "服务器内部错误";
}