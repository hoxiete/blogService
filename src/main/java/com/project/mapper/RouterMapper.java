package com.project.mapper;

import com.project.entity.Menu;

import java.util.List;

public interface RouterMapper {

    List<Menu> getMenuList(Integer roleId);

    List<Menu> getRouterList();
}
