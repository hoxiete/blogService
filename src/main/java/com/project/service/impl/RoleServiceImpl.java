package com.project.service.impl;

import com.project.entity.Role;
import com.project.mapper.RoleMapper;
import com.project.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Override
    public List<Role> getAllRole() {
        return roleMapper.selectAll();
    }
}