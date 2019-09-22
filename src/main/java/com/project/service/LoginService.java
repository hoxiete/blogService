package com.project.service;

import com.project.entity.Menu;
import com.project.entity.User;

import java.util.List;

/**
 * ClassName: LoginService
 * Function:  TODO  登录及权限路由接口
 * Date:      2019/9/22 22:14
 * author     Administrator
 */
public interface LoginService {

    User getUserInfo(String loginName);

    List<Menu> getMenuList(Integer roleId);
}
