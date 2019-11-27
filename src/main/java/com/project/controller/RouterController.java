package com.project.controller;

import com.project.config.log.Log;
import com.project.entity.*;
import com.project.service.AssignService;
import com.project.service.RoleService;
import com.project.service.RouterService;
import com.project.util.JSONUtils;
import com.project.util.Result;
import com.project.util.ResultConstants;
import com.project.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/zhwtf/router")
public class RouterController {

    @Autowired
    private RouterService routerService;
    @Autowired
    private AssignService assignService;
    @Autowired
    private RoleService roleService;

    @GetMapping("/getRouter")
    public Result getMenuList(Integer roleId){
        Map<String,Object> data = new HashMap<>();
        if(!roleService.enableCheck(roleId)){
            throw new MyException(ResultConstants.SC_UNAUTHORIZED,"当前用户角色已停用");
        }
        List<Menu> router = routerService.getMenuList(roleId);
        data.put("router",router);
        return Results.OK(data);
    }

    @Log("菜单查询")
    @GetMapping("/getRouterList")
    public String getRouterList(){
        Map<String,Object> data = new HashMap<>();
        List<Menu> routerList = routerService.getRouterList();
        if( null == routerList && routerList.size() == 0 ){
         throw new MyException(ResultConstants.NOT_FOUND,"路由未找到");
        }
        data.put("router",routerList);
        return JSONUtils.toJson(Results.OK(data));
    }
    @Log("菜单添加")
    @PostMapping("/addPermission")
    public String addPermission(Router router,String operator){
       if(routerService.addPermissionBranch(router,operator)==0){
           throw new MyException(ResultConstants.INTERNAL_SERVER_ERROR,"新增失败");
       }
        return JSONUtils.toJson(Results.OK());
    }
    @Log("菜单修改")
    @PutMapping("/editPermission")
    public String editPermission(Router router, String operator, OrderSortDto sortDto){
        if(routerService.editPermissionBranch(router,operator,sortDto)==0){
            throw new MyException(ResultConstants.INTERNAL_SERVER_ERROR,"修改失败");
        }
        return JSONUtils.toJson(Results.OK());
    }
    @Log("菜单状态修改")
    @PutMapping("/removePermission")
    public String deleteUser(Router router){
        if(routerService.deleteByPermId(router)==0){
            throw new MyException(ResultConstants.INTERNAL_SERVER_ERROR,"删除失败");
        }
        return JSONUtils.toJson(Results.OK());
    }
    @Log("菜单批量停用")
    @PutMapping("/batchRemovePermissions")
    public String deleteUserBatch(Integer[] permIds){
        if(permIds.length!=0) {
            routerService.deleteByBatchPermId(permIds);
        }
        return JSONUtils.toJson(Results.OK());
    }

    @Log("权限菜单获取")
    @GetMapping("/getPermissionTree")
    public Result getPermissionTree(){
        List<Menu> permissionTree = routerService.getPermissionTree();
        return Results.OK(permissionTree);
    }
    @GetMapping("/getPermissionIdByRoleId")
    public Result getPermissionIdByRoleId(Integer roleId){
        List<Integer> list = assignService.getPermissionIdByRoleId(roleId);
        return Results.OK(list);
    }
    @Log("权限修改")
    @PostMapping("/addRoleAssgin")
    public Result addRoleAssgin(Integer[] permIds ,Integer roleId,String operater){
        if(assignService.addRoleAssgin(permIds,roleId,operater)==0){
            throw new MyException(ResultConstants.INTERNAL_SERVER_ERROR,"修改失败");
         }
        return Results.OK();
    }
}
