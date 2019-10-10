package com.project.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.project.entity.Blog;
import com.project.entity.Dict;
import com.project.entity.MyException;
import com.project.mapper.BlogMapper;
import com.project.service.BlogService;
import com.project.util.QiniuCloudUtil;
import com.project.util.ResultConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogMapper blogMapper;

    @Value("${qiniu.domain}")
    private String domain;

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
    public List<Map<String, String>> uploadImg(MultipartFile[] imgs) {
        List<Map<String, String>> list = new ArrayList<>();
        if(imgs.length!=0){
            String fileName = null;
            try {
                for(MultipartFile img : imgs) {
                    Map<String,String> data = new HashMap<>();
                    fileName = QiniuCloudUtil.put64image(img);
                    data.put("pos",img.getOriginalFilename());
                    data.put("url",domain + fileName);
                    list.add(data);
                }
            } catch (Exception e) {
                throw new MyException(ResultConstants.INTERNAL_SERVER_ERROR,"图片上传失败");
            }

        }else{
            throw new MyException(ResultConstants.INTERNAL_SERVER_ERROR,"没有识别到图片");
        }
        return list;
    }

    @Override
    public int removeBlog(Blog blog) {
        Blog coverBlog = new Blog();
        coverBlog.setId(blog.getId());
        coverBlog.setDeleteFlag(blog.getDeleteFlag());
        return blogMapper.updateByPrimaryKeySelective(coverBlog);
    }

    @Override
    public List<Dict> getBlogType() {
        return blogMapper.getBlogType();
    }
}
