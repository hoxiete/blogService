package com.project.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.project.config.redis.PutRedis;
import com.project.constants.UploadConstants;
import com.project.constants.UserRequest;
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
import tk.mybatis.mapper.weekend.Weekend;
import java.io.IOException;
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
        //?????????????????????????????????
        List<String> nowImages =Arrays.asList(StringUtils.substringsBetween(blog.getBody(),baseurl,")")) ;
        //???????????????????????????????????????
        List<Image> oldImages = blogMapper.getImageListByBlog(blog.getId());
        String newImages = "";
        //????????????
        for(Image image : oldImages){
            //????????????????????????????????????????????????????????????
            if(nowImages.contains(image.getImageUrl())){
                newImages += image.getRecourseId() + "," ;
            }else {
                //???????????????????????????????????????????????????????????????????????????????????????????????????
                fastDFSClient.deleteBlogImage(image.getImageUrl());
                //??????????????????
                Image delImage = new Image();
                delImage.setRecourseId(image.getRecourseId());
                if(uploadMapper.delete(delImage)==0){
                    throw new MyException(ResultConstants.INTERNAL_SERVER_ERROR,"???????????????");
                }
            }
        }
        //?????????????????????????????????????????????id???????????????
        if(blog.getImgs()!=null){
            String insertImgs = blog.getImgs();
            newImages += insertImgs;
        }else {
            //???????????????????????????????????? ???
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
//                throw new MyException(ResultConstants.INTERNAL_SERVER_ERROR,"??????????????????");
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
//                throw new MyException(ResultConstants.INTERNAL_SERVER_ERROR,"??????????????????");
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
            throw new MyException(500,"????????????");
        }
        if(image.isPresent()){
            Image updateImg = image.get();
            fastDFSClient.deleteBlogImage(updateImg.getImageUrl());
            updateImg.setImageUrl(storePath.getFullPath());
            Weekend<Image> weekend = new Weekend(Image.class);
            weekend.weekendCriteria().andEqualTo(Image::getRecourseId,updateImg.getRecourseId());
            uploadMapper.updateByExampleSelective(updateImg, weekend);

        }else{
            String userName = UserRequest.getCurrentUser();
            Long resouceId = System.currentTimeMillis();
            blogMapper.updateByPrimaryKeySelective(Blog.builder().id(id).coverImg(resouceId).build());
            uploadMapper.insert(Image.builder()
                    .recourseId(resouceId)
                    .imageType(UploadConstants.blogImg)
                    .imageUrl(storePath.getFullPath())
                    .deleteFlag(0).
                    createUser(userName)
                    .createTime(new Date())
                    .updateUser(userName)
                    .updateTime(new Date()).build());
        }
        return fastDFSClient.getThumbImagePath(storePath);
    }


    @Override
    @PutRedis(key = "blogType")
    public List<Dict> getBlogType() {
        return blogMapper.getBlogType();
    }
    //????????????????????????
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
