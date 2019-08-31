package com.project.mapper;



import com.github.pagehelper.Page;
import com.project.entity.User;
import tk.mybatis.mapper.common.Mapper;


public interface UserMapper extends Mapper<User> {
    public Page<User> selectUserList();

}
