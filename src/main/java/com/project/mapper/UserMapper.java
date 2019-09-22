package com.project.mapper;



import com.github.pagehelper.Page;
import com.project.entity.Image;
import com.project.entity.Menu;
import com.project.entity.User;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


public interface UserMapper extends Mapper<User> {

    User selectUser(String loginName);

    public Page<User> selectUserList();


    List<Menu> getMenuList(Integer roleId);

    Image getHeadUrl(Integer userId);

    User selectUserById(Integer userId);
}
