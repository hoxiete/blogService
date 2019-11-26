package com.project.service.impl;

import com.project.entity.InterviewEntity;
import com.project.mapper.SystemMapper;
import com.project.service.SystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ClassName: SystemServiceImpl
 * Function:  系统服务
 * Date:      2019/11/25 20:21
 * author     Administrator
 */
public class SystemServiceImpl implements SystemService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${interviewKey}")
    private String interviewKey;

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SystemMapper systemMapper;

    @Override
    @Scheduled(cron = "0 0 3 * * *")
    public void timeToSaveTrace() {
        logger.info("开启保存访客定时器：" + LocalDateTime.now());
        List<InterviewEntity> record = (List<InterviewEntity>) redisTemplate.opsForValue().get(interviewKey);
        if (null != record && record.size() > 0) {
            systemMapper.insertInterviewOfDay(record);
        }

    }
}
