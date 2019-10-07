package com.project.mapper;


import com.project.entity.Assign;
import com.project.entity.Menu;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface AssignMapper extends Mapper<Assign> {

    List<Menu> getPermIdByRoleId(Integer roleId);

    int deleteRoleAssgin(Integer roleId);
}
