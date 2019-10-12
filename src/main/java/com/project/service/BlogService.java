package com.project.service;

import com.project.entity.Blog;
import com.project.entity.Dict;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface BlogService {
    List<Blog> searchBlog(Blog blog,Integer pageNum,Integer pageSize);

    int editBlog(Blog blog,String operator);

    List<Dict> getBlogType();

    int addBlog(Blog blog, String operator);

    List<Map<String, String>> uploadImg(MultipartFile[] file,Integer userId,String operator);

    int removeBlog(Blog blog);
}
