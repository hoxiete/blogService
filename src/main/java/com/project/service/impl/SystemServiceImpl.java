package com.project.service.impl;

import com.project.config.redis.RedisManager;
import com.project.entity.InterviewEntity;
import com.project.mapper.SystemMapper;
import com.project.service.SystemService;
import com.project.constants.RedisKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * ClassName: SystemServiceImpl
 * Function:  系统服务
 * Date:      2019/11/25 20:21
 * author     Administrator
 */
@Service
@EnableScheduling
public class SystemServiceImpl implements SystemService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private String interviewKey = RedisKey.interviewKey;

    @Autowired
    private RedisManager redisManager;
    @Autowired
    private SystemMapper systemMapper;

    @Override
    @Scheduled(cron = "0 0 3 * * ?")
    public void timeToSaveTrace() {
        logger.info("开启保存访客定时器：" + LocalDateTime.now());
        Optional.ofNullable(redisManager.getList(interviewKey)).ifPresent(list ->{
            List<InterviewEntity> interviewList = list.stream().map(object -> (InterviewEntity) object).collect(Collectors.toList());
            systemMapper.insertInterviewOfDay(interviewList);
            redisManager.del(interviewKey);
        });
    }
}
