package com.project.service;

import com.project.entity.Menu;
import com.project.entity.Router;

import java.util.List;

public interface RouterService {

    List<Menu> getMenuList(Integer roleId);

    List<Menu> getRouterList();


}
