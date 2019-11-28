package com.project.service;

import com.project.entity.InterviewEntity;
import com.project.entity.SystemInputDto;

import java.util.List;

public interface InterviewService {


    List<InterviewEntity> getInterviewRecord(SystemInputDto dto, Integer pageNum, Integer pageSize);

}
