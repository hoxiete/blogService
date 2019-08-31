package com.project.service.impl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.project.entity.User;
import com.project.mapper.UserMapper;
import com.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper usermapper;

    @Override
    public List<User> selectUserList(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        Page<User> list =usermapper.selectUserList();
        return list;

    }

    @Override
    public User getUserInfo(String loginName) {
        User user = new User();
        user.setLoginName(loginName);
        return usermapper.selectOne(user);
    }
}
