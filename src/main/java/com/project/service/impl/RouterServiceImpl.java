package com.project.service.impl;

import com.project.entity.Menu;
import com.project.entity.Meta;
import com.project.entity.Router;
import com.project.mapper.RouterMapper;
import com.project.service.RouterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RouterServiceImpl implements RouterService {
    @Autowired
    private RouterMapper routerMapper;

    //注入springboot自动配置好的redisTemplate
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    @Override
    public List<Menu> getMenuList(Integer roleId) {
        //字符串序列化器
        RedisSerializer redisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(redisSerializer);
        String key = "Menu"+roleId;

        //先查redis，如果没有就查数据库并存在redis中
        List<Menu> menus = (List<Menu>) redisTemplate.opsForValue().get(key);
        //双重检测
        if(null == menus) {
            synchronized (this) {
                menus = (List<Menu>) redisTemplate.opsForValue().get(key);
                if (null == menus) {
                    menus = routerMapper.getMenuList(roleId);
                    redisTemplate.opsForValue().set(key, menus);
                }
            }
        }
        boolean showMenus = true;
        List<Menu> router = createRouter(menus,showMenus);
        return router;
    }
    @Override
    public List<Menu> getRouterList() {
        List<Menu> list = routerMapper.getRouterList();
        boolean showMenus = false;
        List<Menu> treeRouter = createRouter(list,showMenus);
        return treeRouter;
    }

    @Override
    public Menu getPermissionTree() {
        Menu menu = new Menu();
        menu.setChildren(routerMapper.getPermissionTree());
        menu.setPermId(0);
        menu.setName("根节点");
        return menu;
    }

    @Override
    public int addPermissionBranch(Router router) {
        return 0;
    }

    private List<Menu> createRouter(List<Menu> menus,boolean showMenus) {

        List<Menu> routers = new ArrayList<>();
        for (int i = 0; i < menus.size(); i++) {
            Menu menu = menus.get(i);
            if (showMenus) {
                menu.setMeta(new Meta(menu.getName()));
            }
            if (menu.getParentId() == 0) {
                routers.add(menu);
            }
        }
        List<Menu> menu = createChild(routers,menus,showMenus);

        return menu;
        }

    private List<Menu> createChild(List<Menu> routers, List<Menu> menus ,boolean showMenus ) {
        List<Menu> newRouter = new ArrayList<>();

        for (int i = 0; i < routers.size(); i++) {
            List<Menu> children = new ArrayList<>();
            for (int j = 0; j < menus.size(); j++) {

                    if (routers.get(i).getPermId().equals(menus.get(j).getParentId())) {
                        Menu childmenu = new Menu();
                        if (showMenus) {
                            childmenu.setMeta(menus.get(j).getMeta());
                            childmenu.setPath(menus.get(j).getPath());
                        } else {
                            childmenu.setPath(menus.get(j).getPath());
                        }
                        childmenu.setDeleteFlag(menus.get(j).getDeleteFlag());
                        childmenu.setDescription(menus.get(j).getDescription());
                        childmenu.setName(menus.get(j).getName());
                        childmenu.setChildren(menus.get(j).getChildren());
                        childmenu.setIconCls(menus.get(j).getIconCls());
                        childmenu.setPermId(menus.get(j).getPermId());
                        childmenu.setParentId(menus.get(j).getParentId());
                        childmenu.setOrderSort(menus.get(j).getOrderSort());
                        children.add(childmenu);
                    }
                }
                Menu child = new Menu();
                child.setMeta(routers.get(i).getMeta());
                child.setDeleteFlag(routers.get(i).getDeleteFlag());
                child.setName(routers.get(i).getName());
                child.setDescription(routers.get(i).getDescription());
                child.setChildren(routers.get(i).getChildren());
                child.setIconCls(routers.get(i).getIconCls());
                child.setParentId(routers.get(i).getParentId());
                child.setPath(routers.get(i).getPath());
                child.setOrderSort(routers.get(i).getOrderSort());
                child.setPermId(routers.get(i).getPermId());
                if (!children.isEmpty()) {
                    child.setChildren(createChild(children, menus, showMenus));
                }

                newRouter.add(child);
            }
            return newRouter;
        }

    }
