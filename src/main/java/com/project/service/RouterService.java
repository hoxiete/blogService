package com.project.service;

import com.project.entity.Menu;
import com.project.entity.Router;

import java.util.List;
import java.util.Map;

public interface RouterService {

    List<Menu> getMenuList(Integer roleId);

    List<Menu> getRouterList();


    Menu getPermissionTree();

    int addPermissionBranch(Router router,String operator);

    int editPermissionBranch(Router router, String operator);

    int deleteByPermId(Integer userId);

    void deleteByBatchPermId(Integer[] userIds);
}
