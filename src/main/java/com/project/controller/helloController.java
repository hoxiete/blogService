package com.project.controller;

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
    @RequestMapping("/hello")
    public String hello(){
        return "helloworld";
    }
}
