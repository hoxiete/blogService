package com.project.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.tobato.fastdfs.domain.fdfs.DefaultThumbImageConfig;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.project.entity.*;
import com.project.mapper.BlogMapper;
import com.project.mapper.UploadMapper;
import com.project.service.BlogService;
import com.project.constants.ResultConstants;
import com.project.util.FastDFSClient;
import com.project.util.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private UploadMapper uploadMapper;
    @Autowired
    private FastDFSClient fastDFSClient;

    @Value("${resource.baseurl}")
    private String baseurl;
    @Value("${imgType.head}")
    private String fileType;

    @Override
    public PageInfo<BlogShowDto> searchBlog(Blog blog, Integer pageNum, Integer pageSize, Integer operatorId) {
        PageHelper.startPage(pageNum,pageSize);
        Page<BlogShowDto> list = blogMapper.selectBlogListPage(blog,operatorId);
        PageInfo<BlogShowDto> page = new PageInfo<>(list);
        page.setList(list.stream().map(dto -> {
            String imageUrl = dto.getImageUrl();
            if(!StringUtils.isEmpty(imageUrl)) {
                dto.setImageUrl(fastDFSClient.getThumbImagePath(imageUrl));
            }
            return dto;
        }).collect(Collectors.toList()));
        return page;
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public int editBlog(Blog blog, String operator) {
        Blog editBlog = new Blog();
        //检索博客，找出所有图片
        List<String> nowImages =Arrays.asList(StringUtils.substringsBetween(blog.getBody(),baseurl,")")) ;
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
                fastDFSClient.deleteBlogImage(image.getImageUrl());
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
//        if(blog.getCoverImg()!=null) {
//            Image img = blogMapper.getCoverImageByBlog(blog.getId());
//            if(img!=null) {
//                fastDFSClient.deleteBlogImage(img.getImageUrl());
//                uploadMapper.delete(img);
//            }
//            editBlog.setCoverImg(blog.getCoverImg());
//        }
        editBlog.setSummary(blog.getSummary());
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

//    @Transactional
//    @Override
//    public List<Map<String, String>> uploadImg(MultipartFile frontCoverImg,MultipartFile[] imgs,Integer userId,String operator) {
//        List<Map<String, String>> list = new ArrayList<>();
//        String fileName = null;
//        String resourceId = null;
//        String filePath = userId + prex;
//        if(frontCoverImg!=null) {
//            Map<String, String> data = new HashMap<>();
//            try {
////                fileName = QiniuCloudUtil.put64image(frontCoverImg, filePath);
//            } catch (Exception e) {
//                throw new MyException(ResultConstants.INTERNAL_SERVER_ERROR,"图片上传失败");
//            }
//            resourceId = blogImgInsert(fileName, fileType, operator);
//            data.put("resourceId", resourceId);
//            list.add(data);
//        }
//        if(imgs.length!=0){
//            try {
//                for(MultipartFile img : imgs) {
//                    Map<String,String> data1 = new HashMap<>();
////                    fileName = QiniuCloudUtil.put64image(img,filePath);
//                    resourceId = blogImgInsert(fileName,fileType,operator);
//                    data1.put("pos",img.getOriginalFilename());
//                    data1.put("url",domain + fileName);
//                    data1.put("resourceId",resourceId);
//                    list.add(data1);
//                }
//            } catch (Exception e) {
//                throw new MyException(ResultConstants.INTERNAL_SERVER_ERROR,"图片上传失败");
//            }
//
//        }
//        return list;
//    }

    @Override
    public int removeBlog(Blog blog) {
        Blog coverBlog = new Blog();
        coverBlog.setId(blog.getId());
        coverBlog.setDeleteFlag(blog.getDeleteFlag());
        return blogMapper.updateByPrimaryKeySelective(coverBlog);
    }

    @Override
    public String updateCoverImg(MultipartFile file, Integer id) {
        Optional<Image> image = Optional.ofNullable(blogMapper.getCoverImageByBlog(id));
        StorePath storePath = null;
        try {
            storePath = fastDFSClient.uploadImageAndCrtThumbImage(file);
        } catch (IOException e) {
            throw new MyException(500,"上传失败");
        }
        if(image.isPresent()){
            Image updateImg = image.get();
            updateImg.setImageUrl(storePath.getFullPath());
            Example example = new Example(Image.class);
            example.createCriteria().andEqualTo("recourseId",updateImg.getRecourseId());
            fastDFSClient.deleteBlogImage(updateImg.getImageUrl());
            uploadMapper.updateByExampleSelective(updateImg, example);

        }else{
            User user = (User) SecurityUtils.getSubject().getPrincipal();
            long resouceId = System.currentTimeMillis();
            blogMapper.updateByPrimaryKeySelective(Blog.builder().coverImg(resouceId).build());
            uploadMapper.insert(Image.builder()
                    .recourseId(resouceId)
                    .imageType(fileType)
                    .imageUrl(storePath.getFullPath())
                    .deleteFlag(0).
                    createUser(user.getLoginName())
                    .createTime(new Date())
                    .updateUser(user.getLoginName())
                    .updateTime(new Date()).build());
        }
        return fastDFSClient.getThumbImagePath(storePath);
    }


    @Override
    public List<Dict> getBlogType() {
        return blogMapper.getBlogType();
    }
    //新增图片表的记录
    private String blogImgInsert(String url,String fileType,String operator) {
        Long recourseId =System.currentTimeMillis();
        Image image = Image.builder().imageUrl(url).imageType(fileType)
                .recourseId(recourseId)
                .deleteFlag(0).createTime(new Date())
                .createUser(operator).updateTime(new Date())
                .updateUser(operator).build();
        this.uploadMapper.insert(image);

        return String.valueOf(recourseId);
    }
}
