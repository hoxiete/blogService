package com.project.service.impl;

import com.project.entity.Router;
import com.project.mapper.RouterMapper;
import com.project.service.RouterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouterServiceImpl implements RouterService {
    @Autowired
    private RouterMapper routerMapper;
    @Override
    public List<Router> getRouterList() {
        return routerMapper.getRouterList();
    }
}
