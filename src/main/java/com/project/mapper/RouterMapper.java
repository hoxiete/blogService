package com.project.mapper;

import com.project.entity.Menu;
import com.project.entity.Router;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface RouterMapper extends Mapper<Router> {

    List<Menu> getMenuList(Integer roleId);

    List<Menu> getRouterList();

    List<Menu> getPermissionTree();
}
