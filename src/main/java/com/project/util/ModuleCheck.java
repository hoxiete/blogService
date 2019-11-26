package com.project.util;

/**
 * ClassName: ModuleCheck
 * Function:  TODO
 * Date:      2019/11/26 19:59
 * author     Administrator
 */
public class ModuleCheck {
    public static String findModule(String url){
        String subUrl = StringUtils.substringBetween(url,"zhwtf/","/");
        switch (subUrl) {
            case "index": return "登录";
            case "user": return "用户模块";
            case "role": return "角色模块";
            case "router": return "菜单权限模块";
            case "blog": return "博客模块";
            case "exception": return "问题模块";
            default: return "未知";
        }
    }
}
