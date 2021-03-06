package com.project.controller;


import com.github.pagehelper.PageInfo;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.project.config.log.Log;
import com.project.entity.*;
import com.project.service.BlogService;
import com.project.constants.Result;
import com.project.constants.ResultConstants;
import com.project.constants.Results;
import com.project.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * ClassName: BlogController
 * Function:  TODO  博客界面接口
 * Date:      2019/10/7 11:56
 * author     Administrator
 */
@RestController
@RequestMapping("/blog")
public class BlogController {
    @Autowired
    private BlogService blogService;

    @Log("博客查询")
    @GetMapping("/searchBlog")
    public Result searchBlog(Blog blog, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "5") Integer pageSize,Integer operatorId){
        Map<String,Object> data = new HashMap<>();
        PageInfo<BlogShowDto> page = blogService.searchBlog(blog,pageNum,pageSize,operatorId);
        data.put("blogs",page.getList());
        data.put("total",page.getTotal());
        data.put("size",page.getSize());
        return Results.OK(data);
    }

    @GetMapping("/getBlogType")
    public Result getBlogType(){
        List<Dict> TypeList = blogService.getBlogType();
        return Results.OK(TypeList);
    }


//    @PostMapping("/saveBlogImg")
//    public Result saveBlogImg(MultipartFile frontCoverImg,MultipartFile[] img,Integer userId,String operator){
//        List<Map<String,String>> imgUrl = blogService.uploadImg(frontCoverImg,img,userId,operator);
//        return Results.OK(imgUrl);
//    }

    @Log("博客新增")
    @PostMapping("/saveBlog")
    public Result saveBlog(Blog blog,String operator){
        if(null!=blog.getId()){
            if(blogService.editBlog(blog,operator)==0){
                throw new MyException(ResultConstants.INTERNAL_SERVER_ERROR,"修改失败");
            }
        }else {
            if (blogService.addBlog(blog, operator) == 0) {
                throw new MyException(ResultConstants.INTERNAL_SERVER_ERROR, "新增失败");
            }
        }
        return Results.OK();
    }

    @Log("博客删除")
    @PutMapping("/removeBlog")
    public Result removeBlog(Blog blog){
        if(blogService.removeBlog(blog)==0){
            throw new MyException(ResultConstants.INTERNAL_SERVER_ERROR,"隐藏失败");
        }
        return Results.OK();
    }

    @PostMapping("/uploadBlogCover")
    public Result uploadBlogCover(MultipartFile file,Integer id){
        String thumbImgUrl = blogService.updateCoverImg(file, id);
        if(StringUtils.isEmpty(thumbImgUrl)){
            throw new MyException(ResultConstants.INTERNAL_SERVER_ERROR,"更新失败");
        }
        return Results.OK(thumbImgUrl);
    }
//    @Log("博客爬取")
//    @PostMapping("/crawlBlog")
//    public Result crawlBlog(Blog blog,String operator){
//        HunterConfig config = HunterConfigContext.getHunterConfig(Platform.CSDN);
//        config.setUid("u011197448")
//                .setExitWay(ExitWayEnum.DURATION)
//                .setCount(10);
//        HunterProcessor hunter = new BlogHunterProcessor(config);
//        System.out.println("程序开始执行：" + new Date());
//        CopyOnWriteArrayList<VirtualArticle> list = hunter.execute();
//        System.out.println("程序执行完毕：" + new Date());
//        return Results.OK();
//    }

    }



