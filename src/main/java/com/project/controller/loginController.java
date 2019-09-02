package com.project.controller;


import com.github.pagehelper.PageInfo;
import com.project.entity.Menu;
import com.project.entity.User;
import com.project.service.UserService;
import com.project.util.JSONUtils;
import com.project.util.JwtUtil;
import com.project.util.MD5Utils;
import com.project.util.Results;
import com.sun.org.apache.bcel.internal.generic.NEW;
import io.jsonwebtoken.JwtBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class loginController {

    @Autowired
    private UserService userService;


    @PostMapping("/login")
    public String login(String loginName,String passWord){
        Map<String,Object> data = new HashMap<>();
        try {
            User user = userService.getUserInfo(loginName);
            if(user!=null && user.getPassWord().equals(MD5Utils.md5(passWord))){
                String token = JwtUtil.buildJWT(user.getUserName());
                data.put("token",token);
                data.put("userInfo",user);
            }else {
                return JSONUtils.toJson(Results.BAD_REQUEST());
            }
        }catch (Exception e){
            e.printStackTrace();
            return JSONUtils.toJson(Results.INTERNAL_SERVER_ERROR());
        }
        return JSONUtils.toJson(Results.OK(data));

    }

    @GetMapping("/getRouter")
    public String getMenuList(Integer roleId){
        Map<String,Object> data = new HashMap<>();
        Menu router = userService.getMenuList(roleId);
        return JSONUtils.toJson(Results.OK(data));
    }





    @GetMapping("/getAllUser")
    public String gerAllUser(){
        Map<String,Object> data = new HashMap<>();
        List<User> list=userService.getAllUser();
        data.put("user",list);
        return JSONUtils.toJson(Results.OK(data));
    }

    @GetMapping("/getUserList")
    public String loginUser(@RequestParam(required = true,defaultValue = "1") Integer pageNum,@RequestParam(required = true,defaultValue = "5") Integer pageSize){
        Map<String,Object> data = new HashMap<>();
        List<User> list=userService.selectUserList(pageNum ,pageSize);
        PageInfo page = new PageInfo(list);
        data.put("user",list);
        int i =1/0;
        data.put("total",page.getTotal());
        data.put("size",page.getSize());
        return JSONUtils.toJson(Results.OK(data));
    }

}


