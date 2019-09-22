package com.project.controller;



import com.project.util.JSONUtils;
import com.project.util.Results;
import com.project.util.SaveImgUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.crypto.Data;
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
        String msg="";
        if (!"".equals(SaveImgUtil.upload(image, baseUrl))){
            // 上传成功，给出页面提示
            msg = "上传成功！";
        }else {
            msg = "上传失败！";
        }
        // 显示图片
        map.put("msg", msg);
        map.put("fileName", image.getOriginalFilename());
       return JSONUtils.toJson(Results.OK(map));
    }

}
