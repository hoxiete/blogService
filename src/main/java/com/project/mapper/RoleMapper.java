package com.project.mapper;

import com.github.pagehelper.Page;
import com.project.entity.Role;
import tk.mybatis.mapper.common.Mapper;

public interface RoleMapper extends Mapper<Role> {
    Page<Role> getRoleListPage(Role role);

    Integer getMaxSort();

    int enableCheck(Integer roleId);
}
