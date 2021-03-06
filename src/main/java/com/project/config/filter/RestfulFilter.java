package com.project.config.filter;

import com.alibaba.fastjson.JSONObject;
import com.project.config.redis.RedisManager;
import com.project.constants.RedisKey;
import com.project.constants.Result;
import com.project.constants.Results;
import com.project.constants.UserRequest;
import com.project.util.JwtUtil;
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
    private RedisManager redisManager;

    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (HttpMethod.OPTIONS.name().equalsIgnoreCase(WebUtils.toHttp(request).getMethod())) {
            return Boolean.TRUE;
        }
        //对token进行校验
        String loginToken = UserRequest.getToken(request);

        Optional<String> userName = JwtUtil.getUserName(loginToken);
        //比较refreshToken的时间戳
        if(userName.isPresent()) {
            // 获取RefreshToken时间戳,及AccessToken中的时间戳
            String refreshTokenCacheKey = RedisKey.REFRESH_TOKEN_PREFIX + userName.get();
            String buildMillis = JwtUtil.getClaims(loginToken, RedisKey.CURRENT_TIME_MILLIS).orElse("");
            String reFreshMillis = (String) redisManager.get(refreshTokenCacheKey);
            //验证token使用者的唯一性，只能允许一个人使用
            if (buildMillis.equals(reFreshMillis)) {
                return true;
            }else{
                logger.warn("token已失效");
                return false;
            }
        }
        else{
                logger.warn("token验证失败");
                return false;

        }
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