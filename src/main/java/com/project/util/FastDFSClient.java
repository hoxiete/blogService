package com.project.util;
import com.github.tobato.fastdfs.domain.fdfs.DefaultThumbImageConfig;
import com.github.tobato.fastdfs.domain.fdfs.StorageNode;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.fdfs.ThumbImageConfig;
import com.github.tobato.fastdfs.domain.proto.storage.StorageUploadFileCommand;
import com.github.tobato.fastdfs.domain.upload.FastFile;
import com.github.tobato.fastdfs.exception.FdfsUnsupportStorePathException;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.github.tobato.fastdfs.service.TrackerClient;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * <p>
 * Description: FastDFS文件上传下载包装类
 * </p>
 * <p>
 * </p>
 */
@Component
public class FastDFSClient {

    private final Logger logger = LoggerFactory.getLogger(FastDFSClient.class);

    @Autowired
    //@Resource(name = "devupload")
    private FastFileStorageClient storageClient;

    @Autowired
    private DefaultThumbImageConfig thumbImageConfig;


    /**
     * 上传文件
     *
     * @param file
     *            文件对象
     * @return 文件访问地址
     * @throws IOException
     */
    public String uploadFile(MultipartFile file) throws IOException {
        StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(),
                FilenameUtils.getExtension(file.getOriginalFilename()), null);
        logger.info("fastDFS上传imgUrl为--------"+storePath.getFullPath());
        return getResAccessUrl(storePath);
    }
    /**
     * 上传Blob文件
     *
     * @param file
     *            文件对象
     * @return 文件访问地址
     * @throws IOException
     */
    public String uploadBlobFile(MultipartFile file) throws IOException {
        String suffix = file.getContentType().substring(file.getContentType().indexOf("/")+1);
        StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(),
                suffix, null);
        logger.info("fastDFS上传imgUrl为-------"+storePath.getFullPath());
        return getResAccessUrl(storePath);
    }
    /**
     * 上传文件
     *
     * @param file
     *            文件对象
     * @return 文件访问地址
     * @throws IOException
     */
//    public String uploadFiletest(MultipartFile file) throws IOException {
//        StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(),
//                FilenameUtils.getExtension(file.getOriginalFilename()), null);
//        FastFile fastfile = (new FastFile.Builder()).withFile(file.getInputStream(), file.getSize(),
//                FilenameUtils.getExtension(file.getOriginalFilename())).build();
//        StorageNode client = new StorageNode("192.168.188.17",23000,(byte)0);
//        StorageUploadFileCommand command = new StorageUploadFileCommand(client.getStoreIndex(), inputStream, fileExtName, fileSize, false);
//        return (StorePath)this.fdfsConnectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
//        return getResAccessUrl(storePath);
//    }

    /**
     * 将一段字符串生成一个文件上传
     *
     * @param content
     *            文件内容
     * @param fileExtension
     * @return
     */
    public String uploadFile(String content, String fileExtension) {
        byte[] buff = content.getBytes(Charset.forName("UTF-8"));
        ByteArrayInputStream stream = new ByteArrayInputStream(buff);
        StorePath storePath = storageClient.uploadFile(stream, buff.length, fileExtension, null);
        logger.info("fastDFS上传imgUrl为---------"+storePath.getFullPath());
        return getResAccessUrl(storePath);
    }

    // 封装图片完整URL地址
    private String getResAccessUrl(StorePath storePath) {
        String fileUrl = storePath.getFullPath();
        return fileUrl;
    }

    /**
     * 传图片并同时生成一个缩略图 "JPG", "JPEG", "PNG", "GIF", "BMP", "WBMP"
     *
     * @param file
     *            文件对象
     * @return 文件访问地址
     * @throws IOException
     */
    public StorePath uploadImageAndCrtThumbImage(MultipartFile file) throws IOException {
        StorePath storePath = storageClient.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(),
                FilenameUtils.getExtension(file.getOriginalFilename()), null);
        logger.info("fastDFS上传imgUrl为---------"+storePath.getFullPath());
        return storePath;
    }
    /**
     * 传图片并同时生成一个缩略图 "JPG", "JPEG", "PNG", "GIF", "BMP", "WBMP"
     *
     * @param file
     *            文件对象
     * @return 文件访问地址
     * @throws IOException
     */
    public String uploadBlobImageAndThumb(MultipartFile file) throws IOException {
        String suffix = file.getContentType().substring(file.getContentType().indexOf("/")+1);
        StorePath storePath = storageClient.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(),
                suffix, null);
        logger.info("fastDFS上传imgUrl为------"+storePath.getFullPath());
        return getResAccessUrl(storePath);
    }

    /**
     * 删除文件
     *
     * @param fileUrl
     *            文件访问地址
     * @return
     */
    public void deleteFile(String fileUrl) {
        if (StringUtils.isEmpty(fileUrl)) {
            return;
        }
        try {
            StorePath storePath = StorePath.parseFromUrl(fileUrl);
            storageClient.deleteFile(storePath.getGroup(), storePath.getPath());
        } catch (FdfsUnsupportStorePathException e) {
            logger.warn(e.getMessage());
        }
    }

    /**
     * 删除博客图片和该缩略图
     *
     * @param fileUrl
     *            文件访问地址
     * @return
     */
    public void deleteBlogImage(String fileUrl) {
        if (StringUtils.isEmpty(fileUrl)) {
            return;
        }
        try {
            StorePath storePath = StorePath.parseFromUrl(fileUrl);
            storageClient.deleteFile(storePath.getGroup(), storePath.getPath());
            storageClient.deleteFile(storePath.getGroup(), thumbImageConfig.getThumbImagePath(storePath.getPath()));
            logger.info("fastDFS删除----------"+storePath.getFullPath());
        } catch (FdfsUnsupportStorePathException e) {
            logger.warn(e.getMessage());
        }
    }

    public String getThumbImagePath(StorePath storePath){
        return thumbImageConfig.getThumbImagePath(storePath.getFullPath());
    }
    public String getThumbImagePath(String path){
        return thumbImageConfig.getThumbImagePath(path);
    }
}