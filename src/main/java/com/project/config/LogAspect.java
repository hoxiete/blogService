package com.project.config;

import com.project.config.log.Log;
import com.project.config.redis.RedisManager;
import com.project.constants.RedisKey;
import com.project.constants.Result;
import com.project.constants.ResultConstants;
import com.project.constants.UserRequest;
import com.project.entity.InterviewEntity;
import com.project.entity.MyException;
import com.project.entity.User;
import com.project.util.*;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@Aspect
@Component
@Order(2)
public class LogAspect {
    public static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    private RedisManager redisManager;

    public String interviewKey = RedisKey.interviewKey;
    //表示匹配带有自定义注解的方法
    @Pointcut("@annotation(com.project.config.log.Log)")
    public void pointcut() {}

    @Around("@annotation(log)")
    public Object around(ProceedingJoinPoint point,Log log)throws Throwable {
        Object result =null;
        long beginTime = System.currentTimeMillis();
        String action = log.value();
        try {
            logger.info("我在目标方法之前执行！");
            result = point.proceed();
            long endTime = System.currentTimeMillis();
            insertLog(action,endTime-beginTime,result);
        } catch (Throwable e) {
            throw e;  //这里抛出的异常会在weblog切面拦截
        }
        return result;
    }

    private void insertLog(String action ,long time,Object result) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String ip = AddressUtils.getIpAddr(request);
        String loginName = "";
        if(action.equals("登录")){
            Result res = (Result) result;
            switch (res.getStatus()){
                case ResultConstants.OK:
                    Map map = (Map) res.getData();
                    User user = (User) map.get("userInfo");
                    loginName = user.getLoginName();
                    break;
                case ResultConstants.BAD_REQUEST:
                    loginName = (String) res.getData();
                    action = "帐号密码错误";
                    break;
                case ResultConstants.USER_LOCKED:
                    loginName = (String) res.getData();
                    action = "登录被锁定";
                    break;
                default:break;
            }
        }else if(action.equals("登出")){
            Result res = (Result) result;
            loginName = (String) res.getData();
        } else{
            loginName = UserRequest.getCurrentUser();
        }
        InterviewEntity record = new InterviewEntity();
        record.setIp(ip);
        record.setUserName(loginName);
        record.setState(action);
        record.setRecordTime(new Date());
        record.setRunTime(time);
        redisManager.pushToList(interviewKey,record);
    }
}
