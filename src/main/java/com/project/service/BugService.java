package com.project.service;

import com.project.entity.BugDto;
import com.project.entity.ExceptionEntity;

import java.util.List;

public interface BugService {

    List<ExceptionEntity> getBugList(BugDto entity, Integer pageNum, Integer pageSize);

    int editBug(BugDto dto);

    void deleteBugBatch(Integer[] ids);
}
