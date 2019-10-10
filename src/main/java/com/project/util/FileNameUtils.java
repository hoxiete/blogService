package com.project.util;

import java.util.UUID;

public class FileNameUtils {
    /**
     * 获取文件后缀
     * @param fileName
     * @return
     */
    public static String getSuffix(String fileName){
        if(fileName.lastIndexOf(".")!=-1) {
            return fileName.substring(fileName.lastIndexOf("."));
        }else {
            return ".blog";
        }
    }

    /**
     * 生成新的文件名
     * @param fileOriginName 源文件名
     * @return
     */
    public static String getFileName(String fileOriginName){
        return getUUID() + FileNameUtils.getSuffix(fileOriginName);
    }

    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }

}