package com.project.config.filter;

import com.alibaba.fastjson.JSONObject;
import com.project.constants.Result;
import com.project.constants.Results;
import com.project.constants.UserRequest;
import com.project.service.TokenManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RestfulFilter extends UserFilter {
    private static final Logger logger = LoggerFactory.getLogger(RestfulFilter.class);
    @Autowired
    private TokenManager tokenManager;

    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (HttpMethod.OPTIONS.name().equalsIgnoreCase(WebUtils.toHttp(request).getMethod())) {
            return Boolean.TRUE;
        }
        //对token进行校验
        String loginToken = getToken(request);
        //手动注入tokenManager实例
//        TokenManager tokenManager = SpringUtil.getBean(TokenManager.class);
        UsernamePasswordToken token = tokenManager.getToken(loginToken);

        if (token != null) {
            try {
                Subject subject = getSubject(request, response);
                if (subject.getPrincipal() == null) {
                    subject.login(token);
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        logger.warn("token验证失败");
        return false;
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

    /**
     * 在访问controller前判断是否登录，返回json，不进行重定向。
     *
     * @param request
     * @param response
     * @return true-继续往下执行，false-该filter过滤器已经处理，不继续执行其他过滤器
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        //这里是个坑，如果不设置的接受的访问源，那么前端都会报跨域错误，因为这里还没到corsConfig里面
        httpServletResponse.setHeader("Access-Control-Allow-Origin", ((HttpServletRequest) request).getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json");
        Result result = Results.SC_UNAUTHORIZED();
        httpServletResponse.getWriter().write(JSONObject.toJSON(result).toString());
        return false;
    }
}