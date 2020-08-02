package com.project.constants;

import com.project.entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.checkerframework.checker.nullness.Opt;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class UserRequest {
    /** 加密次数 */
    public static int HASH_ITERATIONS = 1;

    public static String LOGIN_USER = "login_user";

    public String USER_PERMISSIONS = "user_permissions";

    /** 登陆token(nginx中默认header无视下划线) */
    public static String LOGIN_TOKEN = "token";

    public static final String currentUser = "currentUser";

    public static String getCurrentUser(){
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        String user = (String) request.getAttribute(currentUser);
        //request为shiro包装类,改用shiro获取用户信息
        Subject currentUser = SecurityUtils.getSubject();
        Optional<User> principal = Optional.ofNullable(((User)currentUser.getPrincipal()));
        return principal.map(User::getUserName).orElse("游客");
    }

}
