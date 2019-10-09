package com.project.mapper;

import com.github.pagehelper.Page;
import com.project.entity.Blog;
import tk.mybatis.mapper.common.Mapper;

public interface BlogMapper extends Mapper<Blog> {

    Page<Blog> selectBlogListPage(Blog blog);
}
