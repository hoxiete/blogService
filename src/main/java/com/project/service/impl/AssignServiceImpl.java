package com.project.service.impl;

import com.project.config.redis.DelRedis;
import com.project.config.redis.PutRedis;
import com.project.entity.Assign;
import com.project.entity.Menu;
import com.project.entity.MyException;
import com.project.mapper.AssignMapper;
import com.project.service.AssignService;
import com.project.util.ResultConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ClassName: AssignServiceImpl
 * Function:  TODO  角色权限接口
 * Date:      2019/10/2 10:38
 * author     Administrator
 */
@Service
public class AssignServiceImpl implements AssignService {
    @Autowired
    private AssignMapper assignMapper;


    @Override
    public List<Integer> getPermissionIdByRoleId(Integer roleId) {
        List<Integer> permIds = new ArrayList<>();
        List<Menu> list = assignMapper.getPermIdByRoleId(roleId);
        List<Integer> parentIds = new ArrayList<>();
        List<Integer> exitParentIds = new ArrayList<>();
        for (Menu menu : list) {
            if (menu.getParentId() == 0) {
                parentIds.add(menu.getPermId());
            }
        }
        for (Menu menu : list) {
            for (Integer permId : parentIds) {
                if (menu.getParentId() == permId && !exitParentIds.contains(permId)) {
                    exitParentIds.add(permId);
                }
            }
        }
        for (Menu menu : list) {
            if (!exitParentIds.contains(menu.getPermId())) {
                permIds.add(menu.getPermId());
            }
        }
        return permIds;
    }

    @DelRedis(key = "router",fieldKey ="#roleId")
    @Transactional
    @Override
    public int addRoleAssgin(Integer[] permIds, Integer roleId, String operater) {
       assignMapper.deleteRoleAssgin(roleId);
        int count = 0;
        for (Integer permId : permIds) {
            if (permId != 0) {
                Assign assign = new Assign();
                assign.setRoleId(roleId);
                assign.setPermId(permId);
                assign.setDeleteFlag(0);
                assign.setCreateUser(operater);
                assign.setCreateTime(new Date());
                assign.setUpdateUser(operater);
                assign.setUpdateTime(new Date());
                assignMapper.insertSelective(assign);
                count++;
            }
        }
        if (count != permIds.length-1) {
            throw new MyException(ResultConstants.INTERNAL_SERVER_ERROR, "新增时失败");
        }
        return count;

    }
}