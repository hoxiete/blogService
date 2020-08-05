package com.project.config.shiro;

import java.util.concurrent.atomic.AtomicInteger;


import com.project.config.redis.RedisManager;
import com.project.entity.Token;
import com.project.entity.User;
import com.project.mapper.UserMapper;
import com.project.service.TokenManager;
import com.project.service.UserService;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author: zh
 * @date: 2020/4/05
 * @description: 登陆次数限制
 */
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {

    private static final Logger logger = LoggerFactory.getLogger(RetryLimitHashedCredentialsMatcher.class);


    public static final String DEFAULT_RETRYLIMIT_CACHE_KEY_PREFIX = "shiro:cache:retrylimit:";
    private String keyPrefix = DEFAULT_RETRYLIMIT_CACHE_KEY_PREFIX;
    public static final int expireSeconds = 3600;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisManager redisManager;

    public void setRedisManager(RedisManager redisManager) {
        this.redisManager = redisManager;
    }

    private String getRedisKickoutKey(String username) {
        return this.keyPrefix + username;
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken usernamePasswordToken, AuthenticationInfo info) {

        //获取用户信息
        String loginName = (String)usernamePasswordToken.getPrincipal();
        //获取用户登录次数
        AtomicInteger retryCount = (AtomicInteger)redisManager.get(getRedisKickoutKey(loginName));
        if (retryCount == null) {
            //如果用户没有登陆过,登陆次数加1 并放入缓存
            retryCount = new AtomicInteger(0);
        }
        //从info中获取用户信息
        User user = (User)info.getPrincipals().getPrimaryPrincipal();
        if (retryCount.incrementAndGet() > 5) {
            //如果用户登陆失败次数大于5次 抛出锁定用户异常  并修改数据库字
            if (user != null && user.getDeleteFlag()==0){
                //数据库字段 默认为 0  就是正常状态 所以 要改为2
                //修改数据库的状态字段为锁定
                user.setDeleteFlag(2);
                userService.editUser(user,"sys");
            }
            logger.info("锁定用户" + user.getUserName());
            //抛出用户锁定异常
            throw new LockedAccountException();
        }
        //判断用户账号和密码是否正确
        boolean matches = super.doCredentialsMatch(usernamePasswordToken, info);
        if (matches) {
            //如果正确,从缓存中将用户登录计数 清除
            redisManager.del(getRedisKickoutKey(loginName));
            if(user.getDeleteFlag()==2) {
                user.setDeleteFlag(0);
                userService.editUser(user, "sys");
            }
        }else {
            redisManager.set(getRedisKickoutKey(loginName), retryCount,expireSeconds);
        }
        return matches;
    }

    /**
     * 根据用户名 解锁用户
     * @param username
     * @return
     */
//    public void unlockAccount(String username){
//        User user = userMapper.findByUserName(username);
//        if (user != null){
//            //修改数据库的状态字段为锁定
//            user.setState("0");
//            userMapper.update(user);
//            redisManager.del(getRedisKickoutKey(username));
//        }
//    }

}
