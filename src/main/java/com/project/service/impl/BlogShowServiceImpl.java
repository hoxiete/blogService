package com.project.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.project.entity.*;
import com.project.mapper.BlogShowMapper;
import com.project.service.BlogShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BlogShowServiceImpl implements BlogShowService {
    @Autowired
    private BlogShowMapper blogMapper;


    @Override
    public List<BlogShowDto> searchBlog(Blog blog, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        Page<BlogShowDto> page = blogMapper.selectBlogListPage(blog);
        return page;
    }

    @Override
    public BlogShowDto getBlogDetail(Integer id) {
        BlogShowDto blog = blogMapper.selectBlogById(id);
        return blog;
    }

    @Override
    public List<Dict> getBlogTypeList() {
        return blogMapper.getBlogType();
    }

    @Override
    public List<Blog> getBlogByType(Integer typeId) {
        Blog blog = new Blog();
        blog.setTypeId(typeId);
        return blogMapper.select(blog);
    }

    @Override
    public List<BlogShowDto> loadBlogList(Blog blog,Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        Page<BlogShowDto> page = blogMapper.loadBlogListPage(blog);
        return page;
    }

}
