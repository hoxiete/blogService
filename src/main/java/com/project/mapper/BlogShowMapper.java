package com.project.mapper;

import com.github.pagehelper.Page;
import com.project.entity.Blog;
import com.project.entity.BlogShowDto;
import com.project.entity.Dict;
import com.project.entity.Image;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BlogShowMapper extends Mapper<Blog> {

    Page<BlogShowDto> selectBlogListPage(@Param("blog") Blog blog);

    List<Dict> getBlogType();

    Page<BlogShowDto> loadBlogListPage(@Param("blog") Blog blog);
}
