package com.project.controller;

import com.project.config.log.Log;
import com.project.entity.MyException;
import com.project.service.impl.SystemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: helloController
 * Function:  TODO  图书界面接口
 * Date:      2019/8/28 20:43
 * author     Administrator
 */
@RestController
public class helloController {

    @Autowired
    private SystemServiceImpl systemService;

    @Log("系统异常")
    @GetMapping("/hello")
    public String hello(){
        int i= 1/0;
        return "i";
    }
    @Log("自定义异常")
    @GetMapping("/hellE")
    public String hello1(){

        throw new MyException(1,"sdasdsd", Thread.currentThread().getStackTrace()[1].getMethodName());
    }
    @GetMapping("/saveInterview")
    public String saveInterview(){
        systemService.timeToSaveTrace();
        return "";
    }
}
