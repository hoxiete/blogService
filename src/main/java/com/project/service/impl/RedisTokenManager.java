package com.project.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.project.config.redis.RedisManager;
import com.project.entity.Token;
import com.project.service.TokenManager;
import com.project.util.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class RedisTokenManager implements TokenManager {

    @Autowired
    private RedisManager redisManager;
    private static final String TOKEN_PREFIX = "tokens:";
    /**
     * token过期秒数
     */
    @Value("${token.expire.seconds}")
    private Integer expireSeconds;

    @Override
    public Token saveToken(UsernamePasswordToken usernamePasswordToken) {
        String key = UUID.randomUUID().toString();
        redisManager.set(TOKEN_PREFIX + key, JSONObject.toJSONString(usernamePasswordToken),
                expireSeconds);

        return new Token(key, DateUtils.addSeconds(new Date(), expireSeconds));
    }

    @Override
    public UsernamePasswordToken getToken(String key) {
        String json = (String) redisManager.get(TOKEN_PREFIX + key);
        if (!StringUtils.isEmpty(json)) {
            UsernamePasswordToken usernamePasswordToken = JSONObject.parseObject(json, UsernamePasswordToken.class);
            return usernamePasswordToken;
        }
        return null;
    }

    @Override
    public boolean deleteToken(String key) {
        key = TOKEN_PREFIX + key;
        if (redisManager.hasKey(key)) {
            redisManager.del(key);

            return true;
        }

        return false;
    }
}