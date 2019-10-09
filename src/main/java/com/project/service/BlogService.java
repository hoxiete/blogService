package com.project.service;

import com.project.entity.Blog;
import com.project.entity.Dict;

import java.util.List;

public interface BlogService {
    List<Blog> searchBlog(Blog blog,Integer pageNum,Integer pageSize);

    int editBlog(Blog blog,String operator);

    List<Dict> getBlogType();

    int addBlog(Blog blog, String operator);
}
