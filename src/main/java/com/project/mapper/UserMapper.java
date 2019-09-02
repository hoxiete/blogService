package com.project.mapper;



import com.github.pagehelper.Page;
import com.project.entity.Menu;
import com.project.entity.User;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


public interface UserMapper extends Mapper<User> {
    public Page<User> selectUserList();


    List<Menu> getMenuList(Integer roleId);
}
