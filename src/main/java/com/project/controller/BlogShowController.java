package com.project.controller;

import com.github.pagehelper.PageInfo;
import com.project.config.log.Log;
import com.project.entity.Blog;
import com.project.entity.BlogShowDto;
import com.project.entity.Dict;
import com.project.service.BlogShowService;
import com.project.constants.Result;
import com.project.constants.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("zhwtf/blogShow")
public class BlogShowController {
    @Autowired
    private BlogShowService blogShowService;

    @Log("前台博客查询")
    @GetMapping("/searchBlog")
    public Result searchBlog(Blog blog, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "5") Integer pageSize){
        Map<String,Object> data = new HashMap<>();
        List<BlogShowDto> blogs = blogShowService.searchBlog(blog,pageNum,pageSize);
        PageInfo<BlogShowDto> page = new PageInfo<>(blogs);
        data.put("blogs",blogs);
        data.put("size",page.getSize());
        data.put("total",page.getTotal());
        return Results.OK(data);
    }

    @GetMapping("/getBlogDetail")
    public Result getBlogDetail(Integer id){
        Blog info = blogShowService.getBlogDetail(id);
        return Results.OK(info);
    }

    @GetMapping("/getBlogTypeList")
    public Result getBlogTypeList(){
        List<Dict> info = blogShowService.getBlogTypeList();
        return Results.OK(info);
    }
    @GetMapping("/getBlogByType")
    public Result getBlogByType(Integer typeId){
        List<Blog> info = blogShowService.getBlogByType(typeId);
        return Results.OK(info);
    }
}
