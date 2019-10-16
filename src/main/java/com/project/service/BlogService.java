package com.project.service;

import com.project.entity.Blog;
import com.project.entity.BlogShowDto;
import com.project.entity.Dict;
import com.project.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface BlogService {
    List<BlogShowDto> searchBlog(Blog blog, Integer pageNum, Integer pageSize, Integer operatorId);

    int editBlog(Blog blog,String operator);

    List<Dict> getBlogType();

    int addBlog(Blog blog, String operator);

    List<Map<String, String>> uploadImg(MultipartFile[] file,Integer userId,String operator);

    int removeBlog(Blog blog);

}
