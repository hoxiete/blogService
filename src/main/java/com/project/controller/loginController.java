package com.project.controller;


import com.project.config.log.Log;
import com.project.config.redis.RedisManager;
import com.project.config.shiro.RetryLimitHashedCredentialsMatcher;
import com.project.constants.Result;
import com.project.constants.ResultConstants;
import com.project.constants.Results;
import com.project.constants.UserRequest;
import com.project.entity.MyException;
import com.project.entity.Token;
import com.project.entity.User;
import com.project.service.LoginService;
import com.project.service.TokenManager;
import com.project.service.UserService;
import com.project.util.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/index")
public class loginController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(loginController.class);

//    @Autowired
//    private LoginService loginService;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenManager tokenManager;

    @Log("登录")
    @PostMapping("/login")
    public Result login(String loginName, String passWord) {
        Map<String, Object> data = new HashMap<>();
        /**
         *使用shiro编写认证操作
         */
        //1：获取subject
        Subject subject = SecurityUtils.getSubject();
        //2:封装用户账号和密码
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(loginName, passWord);
        //3:执行登录方法
        try {
            subject.login(usernamePasswordToken);
            //登录成功
            User user = (User) subject.getPrincipal();
            //redis存储用户登录状态
            Token token = tokenManager.saveToken(usernamePasswordToken);
            if(token != null){
                logger.info("用户信息已存入Redis");
            }else {
                logger.info("用户信息未存入Redis");
            }
            data.put("token", token.getToken());
            data.put("expires_in", token.getExpireTime());   //过期时间1小时
            data.put("userInfo", user);
        } catch (UnknownAccountException e) {
            //登录失败用户名不存在
            return Results.BAD_REQUEST();
        } catch (IncorrectCredentialsException e) {
            //登录失败密码错误
            return Results.BAD_REQUEST();
        } catch (LockedAccountException e) {
            //登录被锁定
            return Results.USER_LOCKED();
        } catch (RuntimeException e){
            return  Results.INTERNAL_SERVER_ERROR();
        }
        return Results.OK(data);
}

    @PostMapping("/checkLoginName")
    public Result checkLoginName(String loginName) {
        if (null != userService.checkLoginName(loginName)) {
            return Results.BAD_REQUEST();
        }
        return Results.OK();
    }

    @Log("注册")
    @PostMapping("/register")
    public Result register(User user) {
        if (userService.insertUserByLoginName(user) == 0) {
            throw new MyException(ResultConstants.INTERNAL_SERVER_ERROR, "注册失败");
        }
        return Results.OK();
    }


}


