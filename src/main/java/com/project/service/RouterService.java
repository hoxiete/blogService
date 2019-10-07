package com.project.service;

import com.project.entity.Menu;
import com.project.entity.OrderSortDto;
import com.project.entity.PermDto;
import com.project.entity.Router;

import java.util.List;
import java.util.Map;

public interface RouterService {

    List<Menu> getMenuList(Integer roleId);

    List<Menu> getRouterList();


    List<Menu> getPermissionTree();

    int addPermissionBranch(Router router,String operator);

    int editPermissionBranch(Router router, String operator, OrderSortDto dto);

    int deleteByPermId(Router router);

    void deleteByBatchPermId(Integer[] userIds);
}
