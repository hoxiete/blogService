package com.project.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.project.config.redis.RedisManager;
import com.project.constants.RedisKey;
import com.project.entity.Token;
import com.project.service.TokenManager;
import com.project.util.DateUtils;
import com.project.util.JwtUtil;
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

    @Override
    public Token saveToken(UsernamePasswordToken user) {
        String currTime = String.valueOf(System.currentTimeMillis());
        //传输用的token,有效期10分钟
        String jwt = JwtUtil.buildJWT(user.getUsername(),currTime, 30);
        String tokenKey = RedisKey.TOKEN_PREFIX + jwt;
        String refreshKey = RedisKey.REFRESH_TOKEN_PREFIX + user.getUsername();
        //设置刷新token
        redisManager.set(refreshKey, currTime , RedisKey.A_HOURS);
        //设置用户信息
        redisManager.set(tokenKey, JSONObject.toJSONString(user),RedisKey.A_HOURS);

        return new Token(jwt, DateUtils.addSeconds(new Date(),RedisKey.TEN_MINIUTE));
    }

    @Override
    public UsernamePasswordToken getToken(String key) {
        String json = (String) redisManager.get(RedisKey.TOKEN_PREFIX + key);
        if (!StringUtils.isEmpty(json)) {
            UsernamePasswordToken usernamePasswordToken = JSONObject.parseObject(json, UsernamePasswordToken.class);
            return usernamePasswordToken;
        }
        return null;
    }

    @Override
    public boolean deleteToken(String key) {
        key = RedisKey.TOKEN_PREFIX + key;
        if (redisManager.hasKey(key)) {
            redisManager.del(key);

            return true;
        }

        return false;
    }
}