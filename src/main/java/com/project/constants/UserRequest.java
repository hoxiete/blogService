package com.project.constants;

import com.project.entity.User;
import com.project.service.TokenManager;
import com.project.util.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class UserRequest {
    @Autowired
    private TokenManager tokenManager;
    /** 加密次数 */
    public static int HASH_ITERATIONS = 1;

    public static String LOGIN_USER = "login_user";

    public String USER_PERMISSIONS = "user_permissions";

    /** 登陆token(nginx中默认header无视下划线) */
    public static String LOGIN_TOKEN = "token";

    public static final String currentUser = "currentUser";

    public static String getCurrentUser(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //request为shiro包装类,改用shiro获取用户信息
        Subject currentUser = SecurityUtils.getSubject();
        Optional<User> principal = Optional.ofNullable(((User)currentUser.getPrincipal()));
        return principal.map(User::getLoginName).orElseGet(()-> {
            String token = getToken(request);
            return JwtUtil.getUserName(token).get();
        });
    }

    /**
     * 根据参数或者header获取login-token
     *
     * @param request
     * @return
     */
    public static String getToken(ServletRequest request) {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        String loginToken = httpServletRequest.getParameter(UserRequest.LOGIN_TOKEN);
        if (StringUtils.isBlank(loginToken)) {
            loginToken = httpServletRequest.getHeader(UserRequest.LOGIN_TOKEN);
        }

        return loginToken;
    }

}
