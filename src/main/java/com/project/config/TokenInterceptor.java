package com.project.config;


import com.project.util.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 请求鉴权拦截器
 */
//@Configuration
public class TokenInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean flag = true;
        //排除过滤的 uri 地址
         String LOGIN_URI = "/user/login";

        //无权限时的提示语
         String INVALID_TOKEN = "invalid token";
         String INVALID_USERID = "invalid userId";

        if (LOGIN_URI.equals(request.getRequestURI())) {
            return true;
        }

        // 获取 HTTP HEAD 中的 TOKEN
        String token = request.getHeader("token");
        // 校验 TOKEN
        flag = StringUtils.isNotBlank(token) ? JwtUtil.checkJWT(token) : false;
        // 如果校验未通过，返回 401 状态
        if (!flag)
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        return flag;
    }
}
