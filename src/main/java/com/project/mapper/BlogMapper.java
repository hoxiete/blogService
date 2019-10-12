package com.project.mapper;

import com.github.pagehelper.Page;
import com.project.entity.Blog;
import com.project.entity.Dict;
import com.project.entity.Image;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BlogMapper extends Mapper<Blog> {

    Page<Blog> selectBlogListPage(Blog blog);

    List<Dict> getBlogType();

    List<Image> getImageListByBlog(Integer id);
}
