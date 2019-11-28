package com.project.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.project.entity.InterviewEntity;
import com.project.entity.SystemInputDto;
import com.project.mapper.InterviewMapper;
import com.project.service.InterviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterviewServiceImpl implements InterviewService {

    @Autowired
    private InterviewMapper interviewMapper;

    @Override
    public List<InterviewEntity> getInterviewRecord(SystemInputDto dto, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        Page<InterviewEntity> page = interviewMapper.getInterviewRecord(dto);
        return page;
    }
}
