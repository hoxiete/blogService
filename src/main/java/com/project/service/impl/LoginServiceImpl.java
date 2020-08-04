package com.project.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.project.entity.Image;
import com.project.entity.Menu;
import com.project.entity.Meta;
import com.project.entity.User;
import com.project.mapper.UploadMapper;
import com.project.mapper.UserMapper;
import com.project.service.LoginService;
import com.project.service.UserService;
import com.project.util.SaveImgUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private UserMapper usermapper;


    @Override
    public User getUserByLoginName(String loginName) {
        User user = usermapper.selectOne(User.builder().loginName(loginName).build());
        return user;
    }


}
