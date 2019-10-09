package com.project.controller;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.project.entity.Blog;
import com.project.entity.Dict;
import com.project.entity.MyException;
import com.project.service.BlogService;
import com.project.util.Result;
import com.project.util.ResultConstants;
import com.project.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * ClassName: BlogController
 * Function:  TODO  博客界面接口
 * Date:      2019/10/7 11:56
 * author     Administrator
 */
@RestController
@RequestMapping("/blog")
public class BlogController {
    @Autowired
    private BlogService blogService;

    @GetMapping("/searchBlog")
    public Result searchBlog(Blog blog, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "5") Integer pageSize){
        Map<String,Object> data = new HashMap<>();
        List<Blog> blogs = blogService.searchBlog(blog,pageNum,pageSize);
        PageInfo<Blog> page = new PageInfo<>(blogs);
        data.put("blogs",blogs);
        data.put("total",page.getTotal());
        data.put("size",page.getSize());
        return Results.OK(data);
    }

    @GetMapping("/getBlogType")
    public Result getBlogType(){
        List<Dict> TypeList = blogService.getBlogType();
        return Results.OK(TypeList);
    }

    @PostMapping("/saveBlog")
    public Result saveBlog(Blog blog,String operator){
        if(null!=blog.getId()){
            if(blogService.editBlog(blog,operator)==0){
                throw new MyException(ResultConstants.INTERNAL_SERVER_ERROR,"修改失败");
            }
        }else {
            if (blogService.addBlog(blog, operator) == 0) {
                throw new MyException(ResultConstants.INTERNAL_SERVER_ERROR, "新增失败");
            }
        }
        return Results.OK();
    }

    }



