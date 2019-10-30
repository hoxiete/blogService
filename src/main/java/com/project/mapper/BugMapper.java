package com.project.mapper;

import com.github.pagehelper.Page;
import com.project.entity.BugDto;
import com.project.entity.ExceptionEntity;
import tk.mybatis.mapper.common.Mapper;

public interface BugMapper extends Mapper<ExceptionEntity> {

    Page<ExceptionEntity> getBugList(BugDto entity);
}
