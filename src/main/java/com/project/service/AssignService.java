package com.project.service;



import java.util.List;

public interface AssignService {

    List<Integer> getPermissionIdByRoleId(Integer roleId);

    int addRoleAssgin(Integer[] permIds, Integer roleId,String operater);
}
