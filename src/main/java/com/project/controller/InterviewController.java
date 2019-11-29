package com.project.controller;

import com.github.pagehelper.PageInfo;
import com.project.config.log.Log;
import com.project.entity.BlogShowDto;
import com.project.entity.InterviewEntity;
import com.project.entity.SystemInputDto;
import com.project.service.InterviewService;
import com.project.util.Result;
import com.project.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/zhwtf/interview")
public class InterviewController {

    @Autowired
    private InterviewService interviewService;

    @Log("访问记录获取")
    @GetMapping("/getInterviewRecord")
    public Result getInterviewRecord(SystemInputDto dto, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "5") Integer pageSize){
        Map<String,Object> data = new HashMap<>();
        List<InterviewEntity> record = interviewService.getInterviewRecord(dto,pageNum,pageSize);
        PageInfo<InterviewEntity> page = new PageInfo<>(record);
        data.put("record",record);
        data.put("total",page.getTotal());
        data.put("size",page.getSize());
        return Results.OK(data);
    }
}
