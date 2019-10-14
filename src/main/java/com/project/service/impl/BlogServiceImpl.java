package com.project.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.project.entity.*;
import com.project.mapper.BlogMapper;
import com.project.mapper.UploadMapper;
import com.project.service.BlogService;
import com.project.util.QiniuCloudUtil;
import com.project.util.ResultConstants;
import com.project.util.StringUtils;
import com.sun.org.apache.bcel.internal.generic.DLOAD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private UploadMapper uploadMapper;

    @Value("${qiniu.domain}")
    private String domain;
    @Value("${imgPath.blog}")
    private String prex;
    @Value("${imgType.blog}")
    private String fileType;

    @Override
    public List<BlogShowDto> searchBlog(Blog blog, Integer pageNum, Integer pageSize, Integer operatorId) {
        PageHelper.startPage(pageNum,pageSize);
        Page<BlogShowDto> page = blogMapper.selectBlogListPage(blog,operatorId);
        return page;
    }

    @Transactional
    @Override
    public int editBlog(Blog blog, String operator) {
        Blog editBlog = new Blog();
        //检索博客，找出所有图片
        List<String> nowImages =Arrays.asList(StringUtils.substringsBetween(blog.getBody(),domain,")")) ;
        //获取该博客的所有上传的图片
        List<Image> oldImages = blogMapper.getImageListByBlog(blog.getId());
        String newImages = "";
        //进行比对
        for(Image image : oldImages){
            //如果所有图片里有上传的图片，那么原封不动
            if(nowImages.contains(image.getImageUrl())){
                newImages += image.getRecourseId() + "," ;
            }else {
                //如果所有图片里已经没有了上传的图片，那么就把图片服务器上的图片删除
                QiniuCloudUtil.delete(image.getImageUrl());
                //并更新图片表
                Image delImage = new Image();
                delImage.setRecourseId(image.getRecourseId());
                if(uploadMapper.delete(delImage)==0){
                    throw new MyException(ResultConstants.INTERNAL_SERVER_ERROR,"更新时失败");
                }
            }
        }
        //如果有新的图片上传，那么把图片id添加到末尾
        if(blog.getImgs()!=null){
            String insertImgs = blog.getImgs();
            newImages += insertImgs;
        }else {
            //没有新图片，删掉最后一位 ，
            newImages = newImages.substring(0,newImages.length()-1);
        }
        editBlog.setImgs(newImages);
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

    @Transactional
    @Override
    public List<Map<String, String>> uploadImg(MultipartFile[] imgs,Integer userId,String operator) {
        List<Map<String, String>> list = new ArrayList<>();
        if(imgs.length!=0){
            String fileName = null;
            String resourceId = null;
            try {
                for(MultipartFile img : imgs) {
                    Map<String,String> data = new HashMap<>();
                    String filePath = userId + prex;
                    fileName = QiniuCloudUtil.put64image(img,filePath);
                    resourceId = blogImgInsert(fileName,fileType,operator);
                    data.put("pos",img.getOriginalFilename());
                    data.put("url",domain + fileName);
                    data.put("resourceId",resourceId);
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
    //新增图片表的记录
    private String blogImgInsert(String fullPath,String fileType,String operator) {
        Image image = new Image();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime =formatter.format(new Date());
        image.setImageUrl(fullPath);
        Long recourseId =System.currentTimeMillis();
        image.setImageType(fileType);
        image.setDeleteFlag(0);
        image.setRecourseId(recourseId);
        image.setCreateTime(createTime);
        image.setUpdateUser(operator);
        image.setCreateUser(operator);
        this.uploadMapper.insert(image);

        return String.valueOf(recourseId);
    }
}
