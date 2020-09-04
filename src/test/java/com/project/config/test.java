package com.project.config;

import cn.hutool.core.io.FileUtil;
import com.project.config.redis.RedisManager;
import com.project.entity.Menu;
import com.project.entity.Meta;
import com.project.mapper.RouterMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class test {
    @Autowired
    private RouterMapper routerMapper;

    @Test
    public void runingtest() {
        List<Menu> list = routerMapper.getRouterList();
//        FileUtil.writeLines(list,FileUtil.file("d://tes.txt"),"utf-8");
//        FileUtil.readLines(FileUtil.file("d://tes.txt"),"utf-8");
//        list.add(new Menu());
        List<List<Menu>> addlsit = new ArrayList<>();
        for(int k=0;k<5;k++){
            long startT = System.nanoTime();
            for (int i=0;i<100000;i++){
                addlsit.add(createRouter1(list,true));
            }
            long endT = System.nanoTime();
            System.out.println((endT-startT));
            addlsit = new ArrayList<>();
        }
        for(int k=0;k<5;k++) {
            long startT = System.nanoTime();
            for (int i = 0; i < 100000; i++) {
                addlsit.add(createRouter(list, true));
            }
            long endT = System.nanoTime();
            System.out.println((endT - startT));
            addlsit = new ArrayList<>();
        }
    }

    private List<Menu> createRouter1(List<Menu> menus,boolean showMeta) {
        if(showMeta){
            menus.forEach(menu -> menu.setMeta(new Meta(menu.getName())));
        }
        return menus.stream().filter(menu -> menu.getParentId().equals(0)).map(menu -> createChild1(menu,menus))
                .filter(oneMenu -> !(oneMenu.getPath().equals("/")&&oneMenu.getChildren().size()==0))
                .collect(Collectors.toList());
    }

    private Menu createChild1(Menu menu, List<Menu> menus  ) {
        menu.setChildren(menus.stream().filter(oneMenu -> oneMenu.getParentId().equals(menu.getPermId()))
                .map(oneMenu -> createChild1(oneMenu,menus)).collect(Collectors.toList()));
        return menu;
    }


    private List<Menu> createRouter(List<Menu> menus, boolean showMenus) {

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

        return menu.stream()
                .filter(oneMenu -> !(oneMenu.getPath().equals("/")&&oneMenu.getChildren()==null))
                .collect(Collectors.toList());
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
