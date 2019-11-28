package com.project.service;

import com.project.entity.ExceptionEntity;
import com.project.entity.SystemInputDto;

import java.util.List;

public interface BugService {

    List<ExceptionEntity> getBugList(SystemInputDto entity, Integer pageNum, Integer pageSize);

    int editBug(SystemInputDto dto);

    void deleteBugBatch(Integer[] ids);
}
