package com.project.controller;


import com.project.config.log.Log;
import com.project.config.redis.RedisManager;
import com.project.config.shiro.EmailToken;
import com.project.constants.*;
import com.project.entity.MyException;
import com.project.entity.Token;
import com.project.entity.User;
import com.project.service.LoginService;
import com.project.service.TokenManager;
import com.project.service.UserService;
import com.project.util.EmailUtil;
import com.project.util.ResultMsg;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/index")
@Validated
public class loginController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(loginController.class);

    @Autowired
    private LoginService loginService;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenManager tokenManager;
    @Autowired
    private RedisManager redisManager;

    @Log("密码登录")
    @PostMapping("/loginForPwd")
    public Result loginForPwd(String loginName, String passWord) {
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
            data.put("expires_in", token.getExpireTime());
            data.put("userInfo", user);
        } catch (UnknownAccountException e) {
            //登录失败用户名不存在
            return Results.BAD_REQUEST(loginName);
        } catch (IncorrectCredentialsException e) {
            //登录失败密码错误
            return Results.BAD_REQUEST(loginName);
        } catch (LockedAccountException e) {
            //登录被锁定
            return Results.USER_LOCKED(loginName);
        } catch (RuntimeException e){
            return  Results.INTERNAL_SERVER_ERROR();
        }
        return Results.OK(data);
}
    @Log("邮箱登录")
    @PostMapping("/loginForEmail")
    public Result loginForEmail(@RequestParam("loginName") String email,@RequestParam("passWord") String verifyCode) {
        String code = redisManager.getString(email);
        if(!verifyCode.equals(code)){
            return Results.BAD_REQUEST("验证码错误");
        }else{
            Map<String, Object> data = new HashMap<>();
            //1：获取subject
            Subject subject = SecurityUtils.getSubject();
            //2:封装用户
            EmailToken emailToken = new EmailToken(email);
            //3:执行登录方法
            try {
                subject.login(emailToken);
                //登录成功
                User user = (User) subject.getPrincipal();
                UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(user.getLoginName(),user.getPassWord());
                //redis存储用户登录状态
                Token token = tokenManager.saveToken(usernamePasswordToken);
                if(token != null){
                    logger.info("用户信息已存入Redis");
                }else {
                    logger.info("用户信息未存入Redis");
                }
                data.put("token", token.getToken());
                data.put("expires_in", token.getExpireTime());
                data.put("userInfo", user);
            } catch (RuntimeException e){
                return  Results.INTERNAL_SERVER_ERROR();
            }
            return Results.OK(data);
        }
    }

    @PostMapping("/checkLoginName")
    public Result checkLoginName(@NotBlank String loginName) {
        if (null != userService.checkLoginName(loginName)) {
            return Results.BAD_REQUEST();
        }
        return Results.OK();
    }

    @Log("注册")
    @PostMapping("/register")
    public Result register(User user,String checkCode) {
        if(!redisManager.get(user.getEmail()).equals(checkCode)){
            throw new MyException(ResultConstants.BAD_REQUEST, "验证码错误");
        }
        if (userService.insertUserByLoginName(user) == 0) {
            throw new MyException(ResultConstants.INTERNAL_SERVER_ERROR, "注册失败");
        }
        return Results.OK();
    }

    @PostMapping("/registerVerifyCode")
    public Result registerForEmail(@NotBlank String email) {
        String key = EmailUtil.getRandomNumCode(4);
//        EmailUtil.send(email,"hoxiete-注册","【验证码】:"+key+"  (2分钟内有效)");
        redisManager.setString(email,key,120);
        return Results.OK();
    }
    @PostMapping("/loginVerifyCode")
    public Result loginForEmail(@NotBlank String email) {
        String key = EmailUtil.getRandomNumCode(4);
//        EmailUtil.send(email,"hoxiete-登录","【验证码】:"+key+"  \n\n(2分钟内有效)");
        redisManager.setString(email,key,120);
        return Results.OK();
    }

    @Log("登出")
    @PostMapping("/logout")
    public Result logout() {
        String token = UserRequest.getToken(request);
        String currentUser = UserRequest.getCurrentUser();
        //删除token和refreshToken
        tokenManager.deleteToken(token);
        String refreshTokenCacheKey = RedisKey.REFRESH_TOKEN_PREFIX + currentUser;
        redisManager.del(refreshTokenCacheKey);
        //shiro退出
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return Results.OK(currentUser);
    }
    @Log("刷新token")
    @GetMapping("/refreshToken")
    public Result refreshToken(@RequestParam("token") String loginToken) {
            Optional<UsernamePasswordToken> user = Optional.ofNullable(tokenManager.getToken(loginToken));
            //用户信息还在说明 jwttoken过期了
            if(user.isPresent()) {
                Optional<Token> token = refreshToken(user.get(), loginToken);
                if(token.isPresent()) {
                    return Results.OK(token);
                }else{
                    return Results.BAD_REQUEST();
                }
            }else{
                return Results.BAD_REQUEST();
            }
        }

    private Optional<Token> refreshToken(UsernamePasswordToken user, String oldToken) {
        // 获取当前Token的帐号信息
        String userName = user.getUsername();
        String refreshTokenCacheKey = RedisKey.REFRESH_TOKEN_PREFIX + userName;
//         判断Redis中RefreshToken是否存在
        if (redisManager.hasKey(refreshTokenCacheKey)) {
            // 设置RefreshToken中的时间戳为当前最新时间戳
            tokenManager.deleteToken(oldToken);
            Token token = tokenManager.saveToken(user);
            return Optional.ofNullable(token);
        }else{
            return Optional.empty();
        }
    }
}


