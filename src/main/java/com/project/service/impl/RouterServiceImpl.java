package com.project.service.impl;

import com.project.config.redis.DelRedis;
import com.project.entity.*;
import com.project.mapper.RouterMapper;
import com.project.service.RouterService;
import com.project.constants.ResultConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class RouterServiceImpl implements RouterService {
    @Autowired
    private RouterMapper routerMapper;



    @Override
    public List<Menu> getRouterList() {
        List<Menu> list = routerMapper.getRouterList();
        boolean showMenus = false;
        List<Menu> treeRouter = createRouter(list,showMenus);
        return treeRouter;
    }

//    @PutRedis(key = "router",fieldKey ="#roleId")
    @Override
    public List<Menu> getMenuList(Integer roleId) {

        List<Menu> menus = routerMapper.getMenuList(roleId);

        boolean showMenus = true;
        List<Menu> router = createRouter(menus,showMenus);
        return router;
    }

    @Override
    public List<Menu> getPermissionTree() {
        List<Menu> list = routerMapper.getRouterList();
        boolean showMenus = false;
        List<Menu> router = createRouter(list,showMenus);
        Menu menu = new Menu();
        menu.setChildren(router);
        menu.setPermId(0);
        menu.setName("根节点");
        List<Menu> tree = new ArrayList<>();
        tree.add(menu);
        return tree;
    }

//    @DelRedis(key = "router")
    @Override
    public int addPermissionBranch(Router router,String operator) {
        List<Integer> list = routerMapper.getLastPermIdAndSort(router.getParentId());
        Router newPermission = new Router();
        newPermission.setPermId(list.get(0)+1);
        newPermission.setName(router.getName());
        newPermission.setPath(router.getPath());
        newPermission.setParentId(router.getParentId());
        newPermission.setDescription(router.getDescription());
        newPermission.setOrderSort(list.get(1)+1);
        newPermission.setIconCls(router.getIconCls());
        newPermission.setIsButton(0);
        newPermission.setDeleteFlag(router.getDeleteFlag());
        newPermission.setCreateTime(new Date());
        newPermission.setCreateUser(operator);
        newPermission.setUpdateTime(new Date());
        newPermission.setUpdateUser(operator);
        return routerMapper.insert(newPermission);
    }

    @DelRedis(key = "router")
    @Transactional
    @Override
    public int editPermissionBranch(Router router, String operator, OrderSortDto sortDto) {
        Integer startEditSort = null;
        Integer endEditSort = null;
        //如果有排序的变更
        if(sortDto.getMoveId()!=null){
            //如果该菜单行又变更了父级菜单
            if(sortDto.getIsChangeParentId()==1){
               //先变更父级菜单
                Router beforRouter = new Router();
                beforRouter.setPermId(router.getPermId());
                beforRouter.setParentId(router.getParentId());
                beforRouter.setOrderSort(router.getOrderSort());
                routerMapper.updateByPrimaryKeySelective(beforRouter);
            }
               List<Router> routerList =  routerMapper.selectSameLevelPermisson(router.getParentId());
               List<Integer> permIds = new ArrayList<>();
               List<Integer> sorts = new ArrayList<>();
          for(Router permmision : routerList){
              permIds.add(permmision.getPermId());
              sorts.add(permmision.getOrderSort());
              if(sortDto.getMoveId() == permmision.getPermId()){
                  startEditSort = permmision.getOrderSort();
              }
              if(sortDto.getPlaceId() == permmision.getPermId()){
                  endEditSort = permmision.getOrderSort();
              }
          }
                //该菜单行下移
            if(startEditSort<endEditSort) {
                if(sortDto.getIsBefore()==1) {
                    permIds = permIds.subList(permIds.indexOf(sortDto.getMoveId()), permIds.indexOf(sortDto.getPlaceId()));
                    sorts = sorts.subList(sorts.indexOf(startEditSort), sorts.indexOf(endEditSort));
                }else {
                    //如果是下移到最后那么要多包含最后一个
                    permIds = permIds.subList(permIds.indexOf(sortDto.getMoveId()), permIds.indexOf(sortDto.getPlaceId())+1);
                    sorts = sorts.subList(sorts.indexOf(startEditSort), sorts.indexOf(endEditSort)+1);
                }
                Collections.rotate(sorts, 1); //左移数组
            }else {
                //该菜单行上移
                permIds = permIds.subList(permIds.indexOf(sortDto.getPlaceId()), permIds.indexOf(sortDto.getMoveId())+1);
                sorts = sorts.subList(sorts.indexOf(endEditSort),sorts.indexOf(startEditSort)+1);
                Collections.rotate(sorts,-1);  //右移数组
            }
            for(int i = 0 ; i < permIds.size(); i++) {
                Router allEditRouter = new Router();
                allEditRouter.setPermId(permIds.get(i));
                allEditRouter.setOrderSort(sorts.get(i));
                routerMapper.updateByPrimaryKeySelective(allEditRouter);
             }
        }

        Router editPermission = new Router();
        editPermission.setPermId(router.getPermId());
        editPermission.setName(router.getName());
        if(sortDto.getMoveId()==null) {
            editPermission.setParentId(router.getParentId());
            editPermission.setOrderSort(router.getOrderSort());
        }
        editPermission.setPath(router.getPath());
        editPermission.setDescription(router.getDescription());
        editPermission.setIconCls(router.getIconCls());
        editPermission.setDeleteFlag(router.getDeleteFlag());
        editPermission.setUpdateTime(new Date());
        editPermission.setUpdateUser(operator);
        return routerMapper.updateByPrimaryKeySelective(editPermission);

    }

    @DelRedis(key = "router")
    @Override
    public int deleteByPermId(Router router) {
        Router user = new Router();
        user.setPermId(router.getPermId());
        user.setDeleteFlag(router.getDeleteFlag());
        return routerMapper.updateByPrimaryKeySelective(user);
    }

    @DelRedis(key = "router")
    @Transactional
    @Override
    public void deleteByBatchPermId(Integer[] permIds) {
        int i=0;
        for( ;i<permIds.length;i++){
            Router user = new Router();
            user.setPermId(permIds[i]);
            user.setDeleteFlag(1);
            routerMapper.updateByPrimaryKeySelective(user);
        }
        if(i!=permIds.length){
            throw new MyException(ResultConstants.INTERNAL_SERVER_ERROR,"删除未生效");
        }
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
