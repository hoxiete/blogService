package com.project.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.project.entity.BugDto;
import com.project.entity.ExceptionEntity;
import com.project.entity.Role;
import com.project.mapper.BugMapper;
import com.project.service.BugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ClassName: BugServiceImpl
 * Function:   异常服务层
 * Date:      2019/10/30 19:31
 * author     Administrator
 */
@Service
public class BugServiceImpl implements BugService {

    @Autowired
    private BugMapper bugMapper;

    @Override
    public List<ExceptionEntity> getBugList(BugDto entity, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        Page<ExceptionEntity> list = bugMapper.getBugList(entity);
        return list;
    }

    @Override
    public int editBug(Integer id) {
        return 0;
    }

    @Override
    public void deleteBugBatch(Integer[] ids) {

    }
}
