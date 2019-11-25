package com.project.mapper;

import com.project.entity.InterviewEntity;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * ClassName: SystemMapper
 * Function:  TODO  系统接口
 * Date:      2019/11/25 22:17
 * author     Administrator
 */
public interface SystemMapper extends Mapper<InterviewEntity> {

    void insertInterviewOfDay(List<InterviewEntity> record);
}
