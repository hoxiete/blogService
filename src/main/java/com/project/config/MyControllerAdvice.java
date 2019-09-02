package com.project.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: cookie
 * @Date: 2019/9/2 15:09
 * @Description: 全局捕获异常和自定义全局捕获异常
 */
//@ControllerAdvice  //不指定包默认加了@Controller和@RestController都能控制
@ControllerAdvice(basePackages ="com.project.controller")
public class MyControllerAdvice {

    /**
     * 全局异常处理，反正异常返回统一格式的map
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Map<String,Object> exceptionHandler(Exception ex){
        Map<String,Object> map  = new HashMap<String,Object>();
        map.put("status",500);
        map.put("msg",ex.getMessage());
        //发生异常进行日志记录，写入数据库或者其他处理，此处省略
        return map;
    }
}
