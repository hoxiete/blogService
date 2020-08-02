package com.project.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.tobato.fastdfs.domain.fdfs.DefaultThumbImageConfig;
import com.project.entity.*;
import com.project.mapper.BlogShowMapper;
import com.project.service.BlogShowService;
import com.project.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BlogShowServiceImpl implements BlogShowService {
    @Autowired
    private BlogShowMapper blogMapper;

    @Autowired
    private DefaultThumbImageConfig  thumbImageConfig;

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
    public PageInfo<BlogShowDto> loadBlogList(Blog blog, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        Page<BlogShowDto> pageList = blogMapper.loadBlogListPage(blog);
        PageInfo<BlogShowDto> page = new PageInfo<>(pageList);
        List<BlogShowDto> collect = pageList.stream().map(dto -> {
            String imageUrl = dto.getImageUrl();
            if(!StringUtils.isEmpty(imageUrl)) {
                dto.setImageUrl(thumbImageConfig.getThumbImagePath(imageUrl));
            }
            return dto;
        }).collect(Collectors.toList());
        page.setList(collect);
        return page;
    }

}
