package com.project.controller;

import com.project.entity.Role;
import com.project.service.RoleService;
import com.project.util.JSONUtils;
import com.project.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping("/getAllRoleList")
    public String getAllRoleList(){
        Map<String,Object> data = new HashMap<>();
        List<Role> list = roleService.getAllRole();
        data.put("roles",list);
        return JSONUtils.toJson(Results.OK(data));
    }
}
