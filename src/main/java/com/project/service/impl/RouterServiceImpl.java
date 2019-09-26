package com.project.service.impl;

import com.project.entity.Menu;
import com.project.entity.Meta;
import com.project.entity.Router;
import com.project.mapper.RouterMapper;
import com.project.service.RouterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RouterServiceImpl implements RouterService {
    @Autowired
    private RouterMapper routerMapper;

    @Override
    public List<Menu> getMenuList(Integer roleId) {
        List<Menu> menus = routerMapper.getMenuList(roleId);
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

    private List<Menu> createRouter(List<Menu> menus,boolean showMenus) {
        List<Menu> routers = new ArrayList<>();
        List<Integer> skipIndex = new ArrayList<>();
        for(int i = 0; i < menus.size(); i++){
            Menu menu = menus.get(i);
           if(showMenus){
               menu.setMeta(new Meta(menu.getName()));
           }
            if(menu.getParentId()==0){
                routers.add(menu);
                skipIndex.add(i);
            }
        }
        for(Menu router : routers){
            List<Menu> children = new ArrayList<>();
            for(int j = 0; j < menus.size(); j++) {
                if (!skipIndex.contains(j)) {
                    if (router.getPermId().equals(menus.get(j).getParentId())) {
                        if(showMenus){
                            router.setPath("-");
                        }
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
