package com.project.service;

import com.project.entity.User;



/**
 * ClassName: LoginService
 * Function:  TODO  登录接口
 * Date:      2019/9/22 22:14
 * author     Administrator
 */
public interface LoginService {

    User getUserByLoginName(String loginName);

}
