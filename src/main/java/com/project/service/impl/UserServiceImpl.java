package com.project.service.impl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.project.entity.Menu;
import com.project.entity.Meta;
import com.project.entity.User;
import com.project.mapper.UserMapper;
import com.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
//        RedisSerializer redisSerializer = new StringRedisSerializer();
//        redisTemplate.setKeySerializer(redisSerializer);

        //先查redis，如果没有就查数据库并存在redis中
//        List<User> userList = (List<User>) redisTemplate.opsForValue().get("allUser");
        //双重检测
//        if(null == userList) {
//            synchronized (this) {
//                userList = (List<User>) redisTemplate.opsForValue().get("allUser");
//                if (null == userList) {
          List<User> userList =  usermapper.selectAll();
//                    redisTemplate.opsForValue().set("allUser", userList);
//                }
//            }
//        }
        return userList;
    }

    @Override
    public List<Menu> getMenuList(Integer roleId) {
        List<Menu> menus = usermapper.getMenuList(roleId);
        List<Menu> router = createRouter(menus);
        return router;
    }

    private List<Menu> createRouter(List<Menu> menus) {
        List<Menu> routers = new ArrayList<>();
        List<Integer> skipIndex = new ArrayList<>();
        for(int i = 0; i < menus.size(); i++){
            Menu menu = menus.get(i);
            menu.setMeta(new Meta(menu.getName()));
            if(menu.getPid()==0){
                routers.add(menu);
                skipIndex.add(i);
            }
        }
        for(Menu router : routers){
            List<Menu> children = new ArrayList<>();
            for(int j = 0; j < menus.size(); j++) {
                if (!skipIndex.contains(j)) {
                    if (router.getId().equals(menus.get(j).getPid())) {
                        router.setPath("-");
                        children.add(menus.get(j));
                    }
                }
            }
            if(!children.isEmpty()){
               router.setChildren(children);
            }
        }
        return  routers;
    }
}
