package com.project.service.impl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.project.entity.User;
import com.project.mapper.UserMapper;
import com.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper usermapper;

    //注入springboot自动配置好的redisTemplate
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

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

    @Override
    public List<User> getAllUser() {
        //字符串序列化器
        RedisSerializer redisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(redisSerializer);

        //先查redis，如果没有就查数据库并存在redis中
        List<User> userList = (List<User>) redisTemplate.opsForValue().get("allUser");
        //双重检测
        if(null == userList) {
            synchronized (this) {
                userList = (List<User>) redisTemplate.opsForValue().get("allUser");
                if (null == userList) {
                    userList = usermapper.selectAll();
                    redisTemplate.opsForValue().set("allUser", userList);
                }
            }
        }
        return userList;
    }
}
