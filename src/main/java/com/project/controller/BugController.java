package com.project.controller;

import com.github.pagehelper.PageInfo;
import com.project.entity.BugDto;
import com.project.entity.ExceptionEntity;
import com.project.entity.MyException;
import com.project.service.BugService;
import com.project.util.Result;
import com.project.util.ResultConstants;
import com.project.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName: BugController
 * Function:  异常界面接口
 * Date:      2019/10/30 19:14
 * author     Administrator
 */
@RestController
@RequestMapping("/zhwtf/exception")
public class BugController {

    @Autowired
    private BugService bugService;

    @GetMapping("/getBugList")
    public Result getBugList(BugDto entity, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "5") Integer pageSize){
        Map<String,Object> data = new HashMap<>();
        List<ExceptionEntity> list = bugService.getBugList(entity,pageNum,pageSize);
        PageInfo page = new PageInfo(list);
        data.put("exceptions",list);
        data.put("total",page.getTotal());
        data.put("size",page.getSize());
        return Results.OK(data);

    }

    @PutMapping("/removeBug")
    public Result editBug(BugDto dto){
        if(bugService.editBug(dto)==0) {
            throw new MyException(ResultConstants.INTERNAL_SERVER_ERROR,"修改失败");
        }
        return Results.OK();
    }

    @PutMapping("/batchRemoveException")
    public Result deleteBugBatch(Integer[] ids){
        if(ids.length!=0) {
            bugService.deleteBugBatch(ids);
        }
        return Results.OK();
    }
}