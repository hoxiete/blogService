package com.project.service;

import com.project.entity.Blog;

import java.util.List;

public interface BlogService {
    List<Blog> searchBlog(Blog blog,Integer pageNum,Integer pageSize);

    int addBlog(Blog blog,String operator);
}
