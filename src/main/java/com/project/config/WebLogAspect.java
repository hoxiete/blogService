package com.project.config;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import com.project.entity.ExceptionEntity;
import com.project.entity.MyException;
import com.project.service.ExceptionSaveService;
import com.project.util.AddressUtils;
import com.project.constants.UserRequest;
import com.project.util.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * ClassName: WebLogAspect
 * Function:  日志管理
 * Date:      2019/8/29 19:58
 * author     Administrator
 */
@Aspect
@Component
@Order(1)  //指定先走
public class WebLogAspect {

    private Logger logger = LoggerFactory.getLogger(getClass());

    //本切面除异常返回处没有移至controllerAdvice，其它在生产环境均无用，测试切面顺序用

    @Autowired
    private ExceptionSaveService exceptionSaveService;

    @Pointcut("execution(public * com.project.controller..*.*(..))")
    public void webLog(){

    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint)throws UnsupportedEncodingException {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 记录下请求内容
        logger.info("URL : " + request.getRequestURL().toString());
        logger.info("HTTP_METHOD : " + request.getMethod());
        logger.info("IP : " + request.getRemoteAddr());
        Enumeration<String> enu = request.getParameterNames();
        while (enu.hasMoreElements()) {
            String name = (String) enu.nextElement();
            logger.info("name:{},value:{}", name, request.getParameter(name));
        }

    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        logger.info("RESPONSE : " + ret);
    }

    @AfterThrowing(throwing = "ret", pointcut = "webLog()")
    public void doAfterThrowing(Throwable ret) throws UnsupportedEncodingException {          //先走切面再走全局异常配置
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String userName = UserRequest.getCurrentUser();
        // 记录异常信息内容
        String address = AddressUtils.getIpAddr(request);
        ExceptionEntity exception =  new ExceptionEntity();
        if(ret instanceof MyException) {
            MyException myException = (MyException) ret;
            exception.setStatus(myException.getStatus());
            exception.setMessage(myException.getMessage());
            if(myException.getMethod()!=null){
                exception.setMethod(myException.getMethod());
            }
        }else{
            exception.setMessage(ret.getMessage());
        }
       exception.setUserName(userName);
       exception.setUrl(request.getRequestURL().toString());
       exception.setIp(address);
       exception.setRecordTime(new Date());
       exception.setResolveFlag(0);
       exceptionSaveService.saveException(exception);

    }
}
