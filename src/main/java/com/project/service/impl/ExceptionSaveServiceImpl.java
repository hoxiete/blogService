package com.project.service.impl;

import com.project.entity.ExceptionEntity;
import com.project.mapper.ExceptionSaveMapper;
import com.project.service.ExceptionSaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExceptionSaveServiceImpl implements ExceptionSaveService {

    @Autowired
    private ExceptionSaveMapper exceptionSaveMapper;

    @Override
    public void saveException(ExceptionEntity exception) {
        exceptionSaveMapper.insert(exception);
    }
}
