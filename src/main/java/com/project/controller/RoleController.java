package com.project.controller;

import com.github.pagehelper.PageInfo;
import com.project.entity.MyException;
import com.project.entity.OrderSortDto;
import com.project.entity.Role;
import com.project.service.RoleService;
import com.project.util.JSONUtils;
import com.project.util.Result;
import com.project.util.ResultConstants;
import com.project.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping("/getAllRoleList")
    public Result getAllRoleList(){
        List<Role> list = roleService.getAllRole();
        return Results.OK(list);
    }
    @GetMapping("/getRoleListPage")
    public Result getRoleListPage(Role role , @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "5") Integer pageSize){
        Map<String,Object> data = new HashMap<>();
        List<Role> list = roleService.getRoleListPage(role,pageNum,pageSize);
        PageInfo page = new PageInfo(list);
        data.put("roleList",list);
        data.put("total",page.getTotal());
        data.put("size",page.getSize());
        return Results.OK(data);
    }
    @PutMapping("/editRole")
    public Result editRole(Role role, String operator, OrderSortDto sortDto){
        if(roleService.editRole(role,operator,sortDto)==0) {
            throw new MyException(ResultConstants.INTERNAL_SERVER_ERROR,"修改失败");
        }
        return Results.OK();
    }
    @PostMapping("/addRole")
    public Result addRole(Role role,String operator){
        if(roleService.addRole(role,operator)==0) {
            throw new MyException(ResultConstants.INTERNAL_SERVER_ERROR,"新增失败");
        }
        return Results.OK();
    }
    @PutMapping("/delete")
    public Result deleteRole(Integer roleId){
        if(roleService.deleteRole(roleId)==0) {
            throw new MyException(ResultConstants.INTERNAL_SERVER_ERROR,"停用失败");
        }
        return Results.OK();
    }

    @PutMapping("/batchDelete")
    public String deleteRoleBatch(Integer[] roleIds){
        if(roleIds.length!=0) {
            roleService.deleteByBatchRoleId(roleIds);
        }
        return JSONUtils.toJson(Results.OK());
    }
}
