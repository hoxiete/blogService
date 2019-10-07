package com.project.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.project.entity.MyException;
import com.project.entity.OrderSortDto;
import com.project.entity.Role;
import com.project.entity.Router;
import com.project.mapper.RoleMapper;
import com.project.service.RoleService;
import com.project.util.Result;
import com.project.util.ResultConstants;
import com.project.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;


    @Override
    public boolean enableCheck(Integer roleId) {
        Integer deleteFlag =  roleMapper.enableCheck(roleId);
        if(deleteFlag==0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public List<Role> getAllRole() {
        Example example = new Example(Role.class);
        example.setOrderByClause("order_sort asc");
        return roleMapper.selectByExample(example);
    }

    @Override
    public List<Role> getRoleListPage(Role role, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        Page<Role> list = roleMapper.getRoleListPage(role);
        return list;
    }

    @Override
    public int editRole(Role role,String operator,OrderSortDto sortDto) {
        Integer startEditSort = null;
        Integer endEditSort = null;
        //如果有排序的变更
        if(sortDto.getMoveId()!=null){
            List<Role> roleList = getAllRole();
            List<Integer> ids = new ArrayList<>();
            List<Integer> sorts = new ArrayList<>();
            for(Role allRole : roleList){
                ids.add(allRole.getId());
                sorts.add(allRole.getOrderSort());
                if(sortDto.getMoveId() == allRole.getId()){
                    startEditSort = allRole.getOrderSort();
                }
                if(sortDto.getPlaceId() == allRole.getId()){
                    endEditSort = allRole.getOrderSort();
                }
            }
            //该菜单行下移
            if(startEditSort<endEditSort) {
                if(sortDto.getIsBefore()==1) {
                    ids = ids.subList(ids.indexOf(sortDto.getMoveId()), ids.indexOf(sortDto.getPlaceId()));
                    sorts = sorts.subList(sorts.indexOf(startEditSort), sorts.indexOf(endEditSort));
                }else {
                    //如果是下移到最后那么要多包含最后一个
                    ids = ids.subList(ids.indexOf(sortDto.getMoveId()), ids.indexOf(sortDto.getPlaceId())+1);
                    sorts = sorts.subList(sorts.indexOf(startEditSort), sorts.indexOf(endEditSort)+1);
                }
                Collections.rotate(sorts, 1); //左移数组
            }else {
                //该菜单行上移
                ids = ids.subList(ids.indexOf(sortDto.getPlaceId()), ids.indexOf(sortDto.getMoveId())+1);
                sorts = sorts.subList(sorts.indexOf(endEditSort),sorts.indexOf(startEditSort)+1);
                Collections.rotate(sorts,-1);  //右移数组
            }
            for(int i = 0 ; i < ids.size(); i++) {
                Role allEditRole = new Role();
                allEditRole.setId(ids.get(i));
                allEditRole.setOrderSort(sorts.get(i));
                roleMapper.updateByPrimaryKeySelective(allEditRole);
            }
        }

        role.setUpdateUser(operator);
        role.setUpdateTime(new Date());
        return roleMapper.updateByPrimaryKeySelective(role);
    }

    @Override
    public int addRole(Role role,String operator) {
        if(null==role.getName()|| ""==role.getName()) {
            throw new MyException(ResultConstants.BAD_REQUEST,"用户为空");
        }
        Integer maxSort = roleMapper.getMaxSort();
        role.setOrderSort(maxSort+1);
        role.setCreateUser(operator);
        role.setCreateTime(new Date());
        role.setUpdateUser(operator);
        role.setUpdateTime(new Date());
        return roleMapper.insertSelective(role);
    }

    @Override
    public int deleteRole(Integer roleId) {
        Role role = new Role();
        role.setId(roleId);
        role.setDeleteFlag(1);
        return roleMapper.updateByPrimaryKeySelective(role);
    }

    @Override
    public void deleteByBatchRoleId(Integer[] roleIds) {
        int i=0;
        for( ;i<roleIds.length;i++){
            Role user = new Role();
            user.setId(roleIds[i]);
            user.setDeleteFlag(1);
            roleMapper.updateByPrimaryKeySelective(user);
        }
        if(i!=roleIds.length){
            throw new MyException(ResultConstants.INTERNAL_SERVER_ERROR,"删除未生效");
        }
    }
}
