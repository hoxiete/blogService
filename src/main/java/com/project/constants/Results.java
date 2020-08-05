package com.project.constants;

import com.project.util.ResultMsg;

/**
 * ClassName: Results
 * Function:  TODO  Result生成工具类
 * Date:      2019/6/11 22:23
 * author     zhanghao
 */
public class Results{

    protected Results(){}

    //
    // 请求成功，返回200,无数据
    // ----------------------------------------------------------------------------------------------------
    public static Result OK() {
        return new Result(ResultConstants.OK, ResultMsg.OK,null);
    }

    //
    // 请求成功，返回200,有数据
    // ----------------------------------------------------------------------------------------------------
    public static Result OK(Object data) {
        return new Result(ResultConstants.OK, ResultMsg.OK,data);
    }

    //
    // 请求错误，请修正请求，400，无数据
    // ----------------------------------------------------------------------------------------------------
    public static Result BAD_REQUEST() {
        return new Result(ResultConstants.BAD_REQUEST, ResultMsg.BAD_REQUEST, null);
    }
    //
    // 请求错误，请修正请求，400，无数据
    // ----------------------------------------------------------------------------------------------------
    public static Result BAD_REQUEST(String msg) {
        return new Result(ResultConstants.BAD_REQUEST, ResultMsg.BAD_REQUEST, msg);
    }
    //
    // 请求错误，请修正请求，400，无数据
    // ----------------------------------------------------------------------------------------------------
    public static Result USER_LOCKED(String loginName) {
        return new Result(ResultConstants.USER_LOCKED, ResultMsg.USER_LOCKED, loginName);
    }

    //
    // Token验证失败，401，无数据
    // ----------------------------------------------------------------------------------------------------
    public static Result SC_UNAUTHORIZED() {
        return new Result(ResultConstants.SC_UNAUTHORIZED, ResultMsg.SC_UNAUTHORIZED, null);
    }

    //
    // 资源未找到，404，无数据
    // ----------------------------------------------------------------------------------------------------
    public static Result NOT_FOUND() {
        return new Result(ResultConstants.NOT_FOUND, ResultMsg.NOT_FOUND,null);
    }


    //
    // 服务器内部错误，500，无数据
    // ----------------------------------------------------------------------------------------------------
    public static Result INTERNAL_SERVER_ERROR() {
        return new Result(ResultConstants.INTERNAL_SERVER_ERROR, ResultMsg.INTERNAL_SERVER_ERROR,null);
    }
}
