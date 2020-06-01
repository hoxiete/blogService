package com.project.util;

import com.project.constants.UserRequest;
import com.project.entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

public class UserUtils {

    public static User getCurrentUser() {
        User user = (User) getSession().getAttribute(UserRequest.LOGIN_USER);

        return user;
    }

    public static void setUserSession(User user) {
        getSession().setAttribute(UserRequest.LOGIN_USER, user);
    }

//    @SuppressWarnings("unchecked")
//    public static List<Permission> getCurrentPermissions() {
//        List<Permission> list = (List<Permission>) getSession().getAttribute(UserConstants.USER_PERMISSIONS);
//
//        return list;
//    }

//    public static void setPermissionSession(List<Permission> list) {
//        getSession().setAttribute(UserConstants.USER_PERMISSIONS, list);
//    }

    public static Session getSession() {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();

        return session;
    }

}
