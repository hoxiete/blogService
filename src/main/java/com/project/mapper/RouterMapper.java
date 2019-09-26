package com.project.mapper;

import com.project.entity.Menu;
import com.project.entity.Router;

import java.util.List;

public interface RouterMapper {

    List<Menu> getMenuList(Integer roleId);

    List<Menu> getRouterList();
}
