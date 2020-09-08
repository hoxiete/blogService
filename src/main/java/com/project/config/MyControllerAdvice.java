package com.project.config;

import com.project.constants.ResultConstants;
import com.project.entity.MyException;
import com.project.util.HttpUtil;
import com.project.util.JSONUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

import javax.validation.ConstraintViolationException;

/**
 * @Auther: cookie
 * @Date: 2019/9/2 15:09
 * @Description: 全局捕获异常和自定义全局捕获异常
 */
//@ControllerAdvice  //不指定包默认加了@Controller和@RestController都能控制
@ControllerAdvice(basePackages ="com.project.controller")
public class MyControllerAdvice {
    private Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 全局异常处理，反正异常返回统一格式的map
     */
    @ResponseBody
    @ExceptionHandler(value = MyException.class)
    public String MyExceptionHandler(MyException result){
        logger.error(result.getMessage());
        //发生异常进行日志记录，写入数据库或者其他处理，此处省略
        return JSONUtils.toJson(result);
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)    //申明捕获那个异常类
    public String test(Exception e) {
        logger.error(e.getMessage(), e);
        return JSONUtils.toJson(new MyException(ResultConstants.INTERNAL_SERVER_ERROR,"未知错误"));
    }
    @ResponseBody
    @ExceptionHandler(value = ConstraintViolationException.class)
    public String validateNull(ConstraintViolationException e) {
        return JSONUtils.toJson(new MyException(ResultConstants.BAD_REQUEST,e.getMessage().split("\\.")[1]));
    }
}
