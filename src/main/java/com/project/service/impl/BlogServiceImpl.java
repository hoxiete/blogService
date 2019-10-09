package com.project.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.project.entity.Blog;
import com.project.entity.Dict;
import com.project.mapper.BlogMapper;
import com.project.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogMapper blogMapper;

    @Override
    public List<Blog> searchBlog(Blog blog,Integer pageNum,Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        Page<Blog> page = blogMapper.selectBlogListPage(blog);
        return page;
    }

    @Override
    public int editBlog(Blog blog, String operator) {
        Blog editBlog = new Blog();
        editBlog.setId(blog.getId());
        editBlog.setTitle(blog.getTitle());
        editBlog.setTypeId(blog.getTypeId());
        editBlog.setBody(blog.getBody());
        editBlog.setBodyHtml(blog.getBodyHtml());
        editBlog.setUpdateTime(new Date());
        editBlog.setUpdateUser(operator);
        return blogMapper.updateByPrimaryKeySelective(editBlog);
    }

    @Override
    public int addBlog(Blog blog,String operator) {
        blog.setCreateTime(new Date());
        blog.setCreateUser(operator);
        blog.setDeleteFlag(0);
        blog.setUpdateTime(new Date());
        blog.setUpdateUser(operator);
        return blogMapper.insert(blog);
    }

    @Override
    public List<Dict> getBlogType() {
        return blogMapper.getBlogType();
    }
}
