package com.project.config.shiro;

import com.project.entity.Token;
import com.project.entity.User;
import com.project.mapper.UserMapper;
import com.project.service.LoginService;
import com.project.service.RouterService;
import com.project.service.TokenManager;
import com.project.util.UserUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;

import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义Realm
 */
public class UserRealm extends AuthorizingRealm {

    Logger logger = LoggerFactory.getLogger(UserRealm.class);
    @Autowired
    private LoginService loginSerive;


    /**
     * 执行授权逻辑
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        logger.info("授权处理");
        /**
         * 给资源授权
         */
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        //添加授权字符串
        //simpleAuthorizationInfo.addStringPermission("user:add");

        //--------------------认证账号
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
//        User user1 = loginSerive.getUserInfo(loginName);
//        if (user1 == null) {
//            //用户名不存在
//            return null;
//        }
//        //-------------------开始授权
//        List<Permission> permissions = permissionService.getPermissionByUserId(user1.getId());
//        for (Permission per : permissions) {
//            simpleAuthorizationInfo.addStringPermission(per.getName());
//            System.out.println("拥有权限：" + per.getName());
//        }
        return simpleAuthorizationInfo;
    }

    /**
     * 执行认证逻辑
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        logger.info("认证处理");
        /**
         * 判断ShiroRealm逻辑UsernamePasswordToken是否正确
         */
        //1判断用户名
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        User user = loginSerive.getUserByLoginName(usernamePasswordToken.getUsername());
        if (user == null) {
            throw new UnknownAccountException("用户名不存在");
        }
        if (user.getDeleteFlag() == 1) {
            throw new IncorrectCredentialsException("无效状态，请联系管理员");
        }

        //判断密码是否正确
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user, user.getPassWord(), getName());

        //设置用户session
//        UserUtils.setUserSession(user);


        return authenticationInfo;
    }
}