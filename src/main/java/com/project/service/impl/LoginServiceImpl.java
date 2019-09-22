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
    public User getUserInfo(String loginName) {
        User user = usermapper.selectUser(loginName);
        return user;
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
