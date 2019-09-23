package com.project.controller;



import com.project.util.JSONUtils;
import com.project.util.QiniuCloudUtil;
import com.project.util.Results;
import com.project.util.SaveImgUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.crypto.Data;
import java.security.Key;
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

    @PostMapping("/uploadImg")
    public String uploadImg(MultipartFile image){
        Map<String,Object> map = new HashMap<>();
        String key ="";
        try {
             key = QiniuCloudUtil.put64image(image);
//             QiniuCloudUtil.delete("56969d0b873a47d6a41d3c2d7e3a51bd.png");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 显示图片
        map.put("key", key);
        map.put("fileName", image.getOriginalFilename());
       return JSONUtils.toJson(Results.OK(map));
    }

}
