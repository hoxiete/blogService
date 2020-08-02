package com.project.controller;

import com.github.pagehelper.PageInfo;
import com.project.config.log.Log;
import com.project.entity.Blog;
import com.project.entity.BlogShowDto;
import com.project.entity.Dict;
import com.project.entity.User;
import com.project.service.BlogShowService;
import com.project.constants.Result;
import com.project.constants.Results;
import com.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/blogShow")
public class BlogShowController {
    @Autowired
    private BlogShowService blogShowService;
    @Autowired
    private UserService userService;

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

    @GetMapping("/getBlogDetail/{id}")
    public Result getBlogDetail(@PathVariable Integer id){
        BlogShowDto info = blogShowService.getBlogDetail(id);
        return Results.OK(info);
    }

    @Log("网站访问")
    @GetMapping("/site")
    public Result getMySite(){
        User info = userService.getMyInfo();
        return Results.OK(info);
    }

    @GetMapping("/loadList")
    public Result loadBlogList(Blog blog,@RequestParam(defaultValue = "1")Integer pageNum,@RequestParam(defaultValue = "10") Integer pageSize){
        Map<String,Object> data = new HashMap<>();
        PageInfo<BlogShowDto> blogs = blogShowService.loadBlogList(blog,pageNum,pageSize);
        data.put("blogs",blogs.getList());
        data.put("page",blogs.getPageNum());
        data.put("hasNextPage",blogs.isHasNextPage());
        return Results.OK(data);
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
