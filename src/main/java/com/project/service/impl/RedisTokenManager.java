package com.project.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.project.entity.Token;
import com.project.service.TokenManager;
import com.project.util.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class RedisTokenManager implements TokenManager {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private static final String TOKEN_PREFIX = "tokens:";
    /**
     * token过期秒数
     */
    @Value("${token.expire.seconds}")
    private Integer expireSeconds;

    @Override
    public Token saveToken(UsernamePasswordToken usernamePasswordToken) {
        String key = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(TOKEN_PREFIX + key, JSONObject.toJSONString(usernamePasswordToken),
                expireSeconds, TimeUnit.SECONDS);

        return new Token(key, DateUtils.addSeconds(new Date(), expireSeconds));
    }

    @Override
    public UsernamePasswordToken getToken(String key) {
        String json = redisTemplate.opsForValue().get(TOKEN_PREFIX + key);
        if (!StringUtils.isEmpty(json)) {
            UsernamePasswordToken usernamePasswordToken = JSONObject.parseObject(json, UsernamePasswordToken.class);

            return usernamePasswordToken;
        }
        return null;
    }

    @Override
    public boolean deleteToken(String key) {
        key = TOKEN_PREFIX + key;
        if (redisTemplate.hasKey(key)) {
            redisTemplate.delete(key);

            return true;
        }

        return false;
    }
}