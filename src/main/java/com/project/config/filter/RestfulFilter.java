package com.project.config.filter;

import com.alibaba.fastjson.JSONObject;
import com.project.config.redis.RedisManager;
import com.project.constants.RedisKey;
import com.project.constants.Result;
import com.project.constants.Results;
import com.project.constants.UserRequest;
import com.project.entity.Token;
import com.project.service.TokenManager;
import com.project.util.JwtUtil;
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
import java.util.Optional;

public class RestfulFilter extends UserFilter {
    private static final Logger logger = LoggerFactory.getLogger(RestfulFilter.class);
    @Autowired
    private TokenManager tokenManager;
    @Autowired
    private RedisManager redisManager;

    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (HttpMethod.OPTIONS.name().equalsIgnoreCase(WebUtils.toHttp(request).getMethod())) {
            return Boolean.TRUE;
        }
        //对token进行校验
        String loginToken = getToken(request);

        Optional<String> userName = JwtUtil.getUserName(loginToken);
        //jwtToken失效情况,查看redis是否还存有用户信息
        if(!userName.isPresent()) {
            Optional<UsernamePasswordToken> user = Optional.ofNullable(tokenManager.getToken(loginToken));
            //用户信息还在说明 jwttoken过期了
            if(user.isPresent()) {
//                if (userName.equals(token.get().getUsername())) {
//                    long buildTime = Long.parseLong(JwtUtil.getClaims(loginToken, RedisKey.CURRENT_TIME_MILLIS).orElse(""));
//                    long diffTime = (System.currentTimeMillis() - buildTime) / 1000;
//                    if (diffTime > RedisKey.FIVE_MINIUTE) {
                        refreshToken(user.get(),loginToken, response);
//                    }
                    return true;
//                }
            }
        }else{
            return true;
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
    public String getToken(ServletRequest request) {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        String loginToken = httpServletRequest.getParameter(UserRequest.LOGIN_TOKEN);
        if (StringUtils.isBlank(loginToken)) {
            loginToken = httpServletRequest.getHeader(UserRequest.LOGIN_TOKEN);
        }

        return loginToken;
    }
    /**
     * 刷新AccessToken，进行判断RefreshToken是否过期，未过期就返回新的AccessToken且继续正常访问
     */
    private boolean refreshToken(UsernamePasswordToken user,String oldToken,ServletResponse response) {
        // 获取当前Token的帐号信息
//        String userName = JwtUtil.getUserName(token);
//        String refreshTokenCacheKey = RedisKey.REFRESH_TOKEN_PREFIX + userName;
        // 判断Redis中RefreshToken是否存在
//        if (redisManager.hasKey(refreshTokenCacheKey)) {
//            // 获取RefreshToken时间戳,及AccessToken中的时间戳
//            // 相比如果一致，进行AccessToken刷新
//            String currentTimeMillisRedis = (String) redisManager.get(refreshTokenCacheKey);
//            if (String.valueOf(buildMillis).equals(currentTimeMillisRedis)) {
                // 设置RefreshToken中的时间戳为当前最新时间戳
//                String currentTimeMillis = String.valueOf(System.currentTimeMillis());
                tokenManager.deleteToken(oldToken);
                tokenManager.saveToken(user);
                // 刷新AccessToken，为当前最新时间戳
                String token = JwtUtil.buildJWT(user.getUsername(), RedisKey.TEN_MINIUTE);
                // 设置响应的Header头新Token
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                httpServletResponse.setHeader("Authentication", token);
//                httpServletResponse.setHeader("Access-Control-Expose-Headers", SecurityConsts.REQUEST_AUTH_HEADER);

//            }
//        }
        return true;
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
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpServletResponse.getWriter().write(JSONObject.toJSON(result).toString());
        return false;
    }
}