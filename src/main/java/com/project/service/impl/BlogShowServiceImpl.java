package com.project.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.project.entity.*;
import com.project.mapper.BlogMapper;
import com.project.mapper.BlogShowMapper;
import com.project.mapper.UploadMapper;
import com.project.service.BlogService;
import com.project.service.BlogShowService;
import com.project.util.QiniuCloudUtil;
import com.project.util.ResultConstants;
import com.project.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
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
    public Blog getBlogDetail(Integer id) {
        Blog blog = blogMapper.selectByPrimaryKey(id);
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

}
