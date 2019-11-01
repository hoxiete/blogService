package com.project.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.project.entity.BugDto;
import com.project.entity.ExceptionEntity;
import com.project.entity.MyException;
import com.project.entity.Role;
import com.project.mapper.BugMapper;
import com.project.service.BugService;
import com.project.util.ResultConstants;
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
    public int editBug(BugDto dto) {
        ExceptionEntity entity = new ExceptionEntity();
        entity.setId(dto.getId());
        entity.setResolveFlag(dto.getResolveFlag());
        return bugMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public void deleteBugBatch(Integer[] ids) {
        int i=0;
        for( ;i<ids.length;i++){
            ExceptionEntity entity = new ExceptionEntity();
            entity.setId(ids[i]);
            entity.setResolveFlag(1);
            bugMapper.updateByPrimaryKeySelective(entity);
        }
        if(i!=ids.length){
            throw new MyException(ResultConstants.INTERNAL_SERVER_ERROR,"更改未生效");
        }
    }
}
