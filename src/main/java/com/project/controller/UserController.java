package com.project.controller;

import com.github.pagehelper.PageInfo;
import com.project.entity.User;
import com.project.service.UserService;
import com.project.util.JSONUtils;
import com.project.util.Results;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName: UserController
 * Function:  TODO  用户界面接口
 * Date:      2019/9/22 22:11
 * author     Administrator
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("/editSelf")
    public String editUserSelf(User userInfo , MultipartFile img){
        Map<String,Object> data = new HashMap<>();
        User userNow = userService.updateUserSelf(userInfo,img);
        data.put("user",userNow);
        return JSONUtils.toJson(Results.OK(data));
    }




    @ApiOperation("获取所有用户")
    @GetMapping("/getAllUser")
    public String gerAllUser(){
        Map<String,Object> data = new HashMap<>();
        List<User> list=userService.getAllUser();
        data.put("user",list);
        return JSONUtils.toJson(Results.OK(data));
    }

    @GetMapping("/getUserList")
    public String loginUser(@RequestParam(required = true,defaultValue = "1") Integer pageNum, @RequestParam(required = true,defaultValue = "5") Integer pageSize){
        Map<String,Object> data = new HashMap<>();
        List<User> list=userService.selectUserList(pageNum ,pageSize);
        PageInfo page = new PageInfo(list);
        data.put("user",list);
        data.put("total",page.getTotal());
        data.put("size",page.getSize());
        return JSONUtils.toJson(Results.OK(data));
    }

}
