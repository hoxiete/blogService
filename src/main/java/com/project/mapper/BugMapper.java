package com.project.mapper;

import com.github.pagehelper.Page;
import com.project.entity.ExceptionEntity;
import com.project.entity.SystemInputDto;
import tk.mybatis.mapper.common.Mapper;

public interface BugMapper extends Mapper<ExceptionEntity> {

    Page<ExceptionEntity> getBugList(SystemInputDto entity);
}
