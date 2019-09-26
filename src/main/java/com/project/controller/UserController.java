package com.project.controller;

import com.github.pagehelper.PageInfo;
import com.project.entity.MyException;
import com.project.entity.User;
import com.project.entity.UserViewDto;
import com.project.service.UserService;
import com.project.util.JSONUtils;
import com.project.util.ResultConstants;
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
    @ApiOperation("获取所有用户")
    @GetMapping("/getAllUser1")
    public String gerAll1User(){

        throw new MyException(ResultConstants.BAD_REQUEST,"12212");

    }

    @GetMapping("/getUserList")
    public String loginUser(User user,@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "5") Integer pageSize){
        Map<String,Object> data = new HashMap<>();
        List<UserViewDto> list = null;
        list = userService.selectUserList(user,pageNum, pageSize);
        PageInfo page = new PageInfo(list);
        data.put("user",list);
        data.put("total",page.getTotal());
        data.put("size",page.getSize());
        return JSONUtils.toJson(Results.OK(data));
    }

    @PutMapping("/delete")
    public String deleteUser(Integer userId){
        if(userService.deleteUserById(userId)==0){
            throw new MyException(ResultConstants.INTERNAL_SERVER_ERROR,"删除失败");
        }
        return JSONUtils.toJson(Results.OK());
    }

    @PutMapping("/batchDelete")
    public String deleteUserBatch(Integer[] userIds){
        userService.deleteUserByBatchId(userIds);

        return JSONUtils.toJson(Results.OK());
    }

    @PutMapping("/editUser")
    public String editUser(User user,String operator){
        if(userService.editUser(user,operator)==0) {
            throw new MyException(ResultConstants.INTERNAL_SERVER_ERROR,"更新失败");
        }
        return JSONUtils.toJson(Results.OK());
    }
    @PostMapping("/addUser")
    public String addUser(User user,String operator){
        if(userService.addUser(user,operator)==0) {
            throw new MyException(ResultConstants.INTERNAL_SERVER_ERROR,"新增失败");
        }
        return JSONUtils.toJson(Results.OK());
    }

}
