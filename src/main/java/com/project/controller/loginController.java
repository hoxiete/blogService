package com.project.controller;


import com.github.pagehelper.PageInfo;
import com.project.entity.Menu;
import com.project.entity.MyException;
import com.project.entity.User;
import com.project.service.LoginService;
import com.project.service.UserService;
import com.project.util.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/zhwtf/index")
public class loginController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private UserService userService;


    @PostMapping("/login")
    public String login(String loginName,String passWord){
        Map<String,Object> data = new HashMap<>();
        try {
            User user = loginService.getUserInfo(loginName);
            if(user!=null && user.getPassWord().equals(MD5Utils.md5(passWord))){
                String token = JwtUtil.buildJWT(user.getUserName());
                data.put("token",token);
                data.put("expires_in",3600);   //过期时间1小时
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
    @PostMapping("/checkLoginName")
    public Result checkLoginName(String loginName){
        if(null != userService.checkLoginName(loginName)){
            return Results.BAD_REQUEST();
        }
        return Results.OK();
    }
    @PostMapping("/register")
    public Result register(User user){
        if(userService.insertUserByLoginName(user)==0){
            throw new MyException(ResultConstants.INTERNAL_SERVER_ERROR,"注册失败");
        }
        return Results.OK();
    }




}


