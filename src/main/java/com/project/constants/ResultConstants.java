package com.project.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: ResultConstants
 * Function:  TODO   返回结果状态码
 * Date:      2019/6/11 22:29
 * author     liuxin
 */
public class ResultConstants {
    /**
     * 请求已经成功处理
     */
    public static final int OK = 200;

    /**
     * 请求错误，请修正请求
     */
    public static final int BAD_REQUEST = 400;
    /**
     * 登录被锁定
     */
    public static final int USER_LOCKED = 403;

    /**
     * Token验证失败
     */
    public static final int SC_UNAUTHORIZED = 401;

    /**
     * 资源未找到
     */
    public static final int NOT_FOUND = 404;

    /**
     * 服务器内部错误
     */
    public static final int INTERNAL_SERVER_ERROR = 500;


}
