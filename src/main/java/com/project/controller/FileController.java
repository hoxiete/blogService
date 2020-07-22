package com.project.controller;



import com.project.util.FastDFSClient;
import com.project.util.JSONUtils;
import com.project.constants.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping("/uploadImg")
    public String uploadImg(MultipartFile image){
        Map<String,Object> map = new HashMap<>();
        String uploadUrl ="";
        try {
             uploadUrl = fastDFSClient.uploadFile(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 显示图片
        map.put("uploadUrl", uploadUrl);
        map.put("fileName", image.getOriginalFilename());
       return JSONUtils.toJson(Results.OK(map));
    }

}
