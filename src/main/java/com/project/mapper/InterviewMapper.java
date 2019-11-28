package com.project.mapper;

import com.github.pagehelper.Page;
import com.project.entity.InterviewEntity;
import com.project.entity.SystemInputDto;

public interface InterviewMapper {


    Page<InterviewEntity> getInterviewRecord(SystemInputDto dto);
}
