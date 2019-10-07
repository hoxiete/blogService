package com.project.service;

import com.project.entity.OrderSortDto;
import com.project.entity.Role;

import java.util.List;

public interface RoleService {
    List<Role> getAllRole();

    List<Role> getRoleListPage(Role role, Integer pageNum, Integer pageSize);

    int editRole(Role role, String operator, OrderSortDto sortDto);

    int addRole(Role role,String operator);

    int deleteRole(Integer roleId);

    void deleteByBatchRoleId(Integer[] roleIds);

    boolean enableCheck(Integer roleId);
}
