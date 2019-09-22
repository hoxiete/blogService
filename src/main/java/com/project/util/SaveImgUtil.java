package com.project.util;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * 文件上传工具包
 */
public class SaveImgUtil {

    /**
     *
     * @param file 文件
     * @param path 文件存放路径
     * @return
     */
    public static String upload(MultipartFile file, String path){

        //生成新的文件名
        String fileName = FileNameUtils.getFileName(file.getOriginalFilename());

        // 生成图片全路径
        String realPath = path + "/" + fileName;

        File dest = new File(realPath);

        //判断文件父目录是否存在
        if(!dest.getParentFile().exists()){
            dest.getParentFile().mkdir();
        }

        try {
            //保存文件
            file.transferTo(dest);
            return fileName;

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    public static boolean delete(String path){

        File file = new File(path);
        if (file.delete()) {
            return true;
        } else {
            return false;
        }

    }
}