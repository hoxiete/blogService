package com.project.service;

import com.project.entity.Blog;
import com.project.entity.BlogShowDto;
import com.project.entity.Dict;

import java.util.List;

public interface BlogShowService {

    List<BlogShowDto> searchBlog(Blog blog, Integer pageNum, Integer pageSize);

    Blog getBlogDetail(Integer id);

    List<Dict> getBlogTypeList();

    List<Blog> getBlogByType(Integer typeId);
}
