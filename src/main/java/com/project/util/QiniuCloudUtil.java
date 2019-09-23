package com.project.util;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class QiniuCloudUtil {

    // 设置需要操作的账号的AK和SK
    private static String ACCESS_KEY="q4nf5suPwtIgNn69lmWKGQI7F8bAz4h85BdXq5f_";

    private static String SECRET_KEY ="62N4VmDCQWgt31k1rcGhU7uOflw3uyCJlxDQz91C";
    // 要上传的空间

    private static String bucketname="blogbyzh";

    private static String DOMAIN ="py8m27zxx.bkt.clouddn.com";

    // 密钥
    private static Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);

    //创建华南访问连接设置
    private static Configuration configuration = new Configuration(Zone.zone2());

    //生成七牛云token
    public static String getUpToken() {
        return auth.uploadToken(bucketname, null, 3600, new StringMap().put("insertOnly", 1));
    }


//     普通上传
    public static String upload(String filePath, String fileName) throws IOException {

        // 创建上传对象
        UploadManager uploadManager = new UploadManager(configuration);
        try {
            // 调用put方法上传
            String token = auth.uploadToken(bucketname);
            if(StringUtils.isEmpty(token)) {
                System.out.println("未获取到token，请重试！");
                return null;
            }
            Response response = uploadManager.put(filePath, fileName, token);
            // 打印返回的信息
            System.out.println(response.bodyString());
            if (response.isOK()) {
                DefaultPutRet putRet;
                putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                //如果不需要对图片进行样式处理，则使用以下方式即可
                return  DOMAIN +"/"+putRet.key;
//                return DOMAIN + ret.key + "?" + style;
            }
        } catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时打印的异常的信息
            System.out.println(r.toString());
            try {
                // 响应的文本信息
                System.out.println(r.bodyString());
            } catch (QiniuException e1) {
                // ignore
            }
        }
        return null;
    }

    //base64方式上传
    public static String put64image(MultipartFile file) throws Exception {
        try {
            InputStream inputStream = file.getInputStream();
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[600]; //buff用于存放循环读取的临时数据
            int rc = 0;
            while ((rc = inputStream.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            byte[] uploadBytes = swapStream.toByteArray();
            //生成文件名
            String filename = FileNameUtils.getFileName(file.getOriginalFilename());
            //创建上传对象
             UploadManager uploadManager = new UploadManager(configuration);
            try {
                Response response = uploadManager.put(uploadBytes,filename,getUpToken());
                //解析上传成功的结果
                DefaultPutRet putRet;
                putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                return DOMAIN + putRet.key;

            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                }
            }
        } catch (UnsupportedEncodingException ex) {

        }
        return null;

    }
    // 普通删除(暂未使用以下方法，未测试)
    public static void delete(String key){
        // 实例化一个BucketManager对象
        BucketManager bucketManager = new BucketManager(auth,configuration);
        try {
            // 调用delete方法移动文件
            bucketManager.delete(bucketname, key);
        } catch (QiniuException e) {
            // 捕获异常信息
            Response r = e.response;
            System.out.println(r.toString());
        }
    }

}
