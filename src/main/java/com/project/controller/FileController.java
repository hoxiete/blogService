package com.project.controller;



import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.fdfs.ThumbImageConfig;
import com.project.entity.Image;
import com.project.entity.MyException;
import com.project.mapper.UploadMapper;
import com.project.util.FastDFSClient;
import com.project.util.JSONUtils;
import com.project.constants.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: FileController
 * Function:  TODO  文件接口
 * Date:      2019/9/21 15:20
 * author     Administrator
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${baseUrl.img}")
    private String baseUrl;

    @Autowired
    private FastDFSClient fastDFSClient;

    @Autowired
    private UploadMapper uploadMapper;

    @Autowired
    private ThumbImageConfig thumbImageConfig;

    @PostMapping("/uploadImg")
    public String uploadImg(MultipartFile image){
        Map<String,Object> map = new HashMap<>();
        String uploadUrl ="";
        String resouceId ="";
        try {
             uploadUrl = fastDFSClient.uploadFile(image);
//             resouceId = blogImgInsert(uploadUrl,"2","swagger");

        } catch (Exception e) {
            throw new MyException(500,e.getMessage());
        }
        // 显示图片
        map.put("uploadUrl", uploadUrl);
//        map.put("resouceId", resouceId);
        map.put("fileName", image.getOriginalFilename());
       return JSONUtils.toJson(Results.OK(map));
    }
    @PostMapping("/uploadImgWithThumbImage")
    public String uploadImgWithThumbImage(MultipartFile image){
        Map<String,Object> map = new HashMap<>();
        String uploadUrl ="";
        String resouceId ="";
        String thumbImageUrl = "";
        try {
            StorePath storePath = fastDFSClient.uploadImageAndCrtThumbImage(image);
            uploadUrl = storePath.getFullPath();
            thumbImageUrl = thumbImageConfig.getThumbImagePath(storePath.getPath());
            resouceId = blogImgInsert(uploadUrl,"2","swagger");

        } catch (Exception e) {
            throw new MyException(500,e.getMessage());
        }
        // 显示图片
        map.put("uploadUrl", uploadUrl);
        map.put("thumbImageUrl", thumbImageUrl);
        map.put("resouceId", resouceId);
        map.put("fileName", image.getOriginalFilename());
       return JSONUtils.toJson(Results.OK(map));
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
