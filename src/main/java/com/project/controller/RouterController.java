package com.project.controller;

import com.project.entity.Menu;
import com.project.entity.MyException;
import com.project.entity.Router;
import com.project.service.RouterService;
import com.project.util.JSONUtils;
import com.project.util.ResultConstants;
import com.project.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/router")
public class RouterController {

    @Autowired
    private RouterService routerService;

    @GetMapping("/getRouter")
    public String getMenuList(Integer roleId){
        Map<String,Object> data = new HashMap<>();
        List<Menu> router = routerService.getMenuList(roleId);
        data.put("router",router);
        return JSONUtils.toJson(Results.OK(data));
    }

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

    @GetMapping("/getPermissionTree")
    public String getPermissionTree(){
        Map<String,Object> data = new HashMap<>();
        Menu permissionTree = routerService.getPermissionTree();
        data.put("tree",permissionTree);
        return JSONUtils.toJson(Results.OK(data));
    }

    @PostMapping("/addPermission")
    public String addPermission(Router router,String operator){
       if(routerService.addPermissionBranch(router,operator)==0){
           throw new MyException(ResultConstants.INTERNAL_SERVER_ERROR,"新增失败");
       }
        return JSONUtils.toJson(Results.OK());
    }

    @PutMapping("/editPermission")
    public String editPermission(Router router,String operator){
        if(routerService.editPermissionBranch(router,operator)==0){
            throw new MyException(ResultConstants.INTERNAL_SERVER_ERROR,"修改失败");
        }
        return JSONUtils.toJson(Results.OK());
    }
    @PutMapping("/removePermission")
    public String deleteUser(Integer permId){
        if(routerService.deleteByPermId(permId)==0){
            throw new MyException(ResultConstants.INTERNAL_SERVER_ERROR,"删除失败");
        }
        return JSONUtils.toJson(Results.OK());
    }

    @PutMapping("/batchRemovePermissions")
    public String deleteUserBatch(Integer[] permIds){
        if(permIds.length!=0) {
            routerService.deleteByBatchPermId(permIds);
        }
        return JSONUtils.toJson(Results.OK());
    }

}
