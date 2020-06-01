package com.project.constants;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class UserRequest {
    /** 加密次数 */
    public static int HASH_ITERATIONS = 1;

    public static String LOGIN_USER = "login_user";

    public String USER_PERMISSIONS = "user_permissions";

    /** 登陆token(nginx中默认header无视下划线) */
    public static String LOGIN_TOKEN = "token";

    public static final String currentUser = "currentUser";

    public static String getCurrentUser(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String user = (String) request.getAttribute(currentUser);
        return user;
    }

}
