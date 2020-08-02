package com.project.config.fastdfs;

import com.github.tobato.fastdfs.domain.conn.FdfsConnectionManager;
import com.github.tobato.fastdfs.domain.fdfs.*;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadCallback;
import com.github.tobato.fastdfs.domain.proto.storage.StorageDeleteFileCommand;
import com.github.tobato.fastdfs.domain.proto.storage.StorageUploadFileCommand;
import com.github.tobato.fastdfs.domain.proto.storage.StorageUploadSlaveFileCommand;
import com.github.tobato.fastdfs.domain.upload.FastFile;
import com.github.tobato.fastdfs.domain.upload.FastImageFile;
import com.github.tobato.fastdfs.domain.upload.ThumbImage;
import com.github.tobato.fastdfs.exception.FdfsUploadImageException;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.project.entity.MyException;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
@Service("devupload")
@ConditionalOnExpression("environment['spring.profiles.active'].equals('dev')")
public class FastDfsuploadClient implements FastFileStorageClient {
    @Autowired
    protected FdfsConnectionManager fdfsConnectionManager;
    @Autowired
    private ThumbImageConfig thumbImageConfig;

    @Value("${uploadStorageUrl}")
    private String storageUrl;

    @Override
    public StorePath uploadFile(InputStream inputStream, long fileSize, String fileExtName, Set<MetaData> metaDataSet) {
        FastFile file = (new FastFile.Builder()).withFile(inputStream, fileSize, FilenameUtils.getExtension(fileExtName)).build();
        StorageNode client = new StorageNode(storageUrl,23000,(byte)0);
        StorageUploadFileCommand command = new StorageUploadFileCommand(client.getStoreIndex(), inputStream, fileExtName, fileSize, false);
        return (StorePath)this.fdfsConnectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
    }

    @Override
    public StorePath uploadImageAndCrtThumbImage(InputStream inputStream, long fileSize, String fileExtName, Set<MetaData> metaDataSet) {
        FastImageFile fastImageFile = (new FastImageFile.Builder()).withFile(inputStream, fileSize, fileExtName).withThumbImage().build();
        StorageNode client = new StorageNode(storageUrl,23000,(byte)0);
        byte[] bytes = this.inputStreamToByte(fastImageFile.getInputStream());
        StorageUploadFileCommand command = new StorageUploadFileCommand(client.getStoreIndex(), fastImageFile.getInputStream(), fileExtName, fileSize, false);
        StorePath storePath = this.fdfsConnectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
        if (null != fastImageFile.getThumbImage()) {
            this.uploadThumbImage(client, new ByteArrayInputStream(bytes), storePath.getPath(), fastImageFile);
        }
        return storePath;
    }

    @Override
    public StorePath uploadImage(FastImageFile fastImageFile) {
        return null;
    }

    @Override
    public StorePath uploadFile(FastFile fastFile) {
        return null;
    }

    @Override
    public void deleteFile(String filePath) {

    }

    @Override
    public StorePath uploadFile(String groupName, InputStream inputStream, long fileSize, String fileExtName) {
        return null;
    }

    @Override
    public StorePath uploadSlaveFile(String groupName, String masterFilename, InputStream inputStream, long fileSize, String prefixName, String fileExtName) {
        return null;
    }

    @Override
    public Set<MetaData> getMetadata(String groupName, String path) {
        return null;
    }

    @Override
    public void overwriteMetadata(String groupName, String path, Set<MetaData> metaDataSet) {

    }

    @Override
    public void mergeMetadata(String groupName, String path, Set<MetaData> metaDataSet) {

    }

    @Override
    public FileInfo queryFileInfo(String groupName, String path) {
        return null;
    }

    @Override
    public void deleteFile(String groupName, String path) {
        StorageNodeInfo client = new StorageNodeInfo(storageUrl,23000);
        StorageDeleteFileCommand command = new StorageDeleteFileCommand(groupName, path);
        this.fdfsConnectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
    }

    @Override
    public <T> T downloadFile(String groupName, String path, DownloadCallback<T> callback) {
        return null;
    }

    @Override
    public <T> T downloadFile(String groupName, String path, long fileOffset, long fileSize, DownloadCallback<T> callback) {
        return null;
    }
    private byte[] inputStreamToByte(InputStream inputStream) {
        try {
            return IOUtils.toByteArray(inputStream);
        } catch (IOException var3) {
            throw new FdfsUploadImageException("upload ThumbImage error", var3.getCause());
        }
    }
    private void uploadThumbImage(StorageNode client, InputStream inputStream, String masterFilename, FastImageFile fastImageFile) {
        ByteArrayInputStream thumbImageStream = null;
        ThumbImage thumbImage = fastImageFile.getThumbImage();

        try {
            thumbImageStream = this.generateThumbImageStream(inputStream, thumbImage);
            long fileSize = (long)thumbImageStream.available();
            String prefixName = thumbImage.getPrefixName();
            StorageUploadSlaveFileCommand command = new StorageUploadSlaveFileCommand(thumbImageStream, fileSize, masterFilename, prefixName, fastImageFile.getFileExtName());
            this.fdfsConnectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
        } catch (IOException var14) {
            throw new FdfsUploadImageException("upload ThumbImage error", var14.getCause());
        } finally {
            IOUtils.closeQuietly(thumbImageStream);
        }

    }
    private ByteArrayInputStream generateThumbImageStream(InputStream inputStream, ThumbImage thumbImage) throws IOException {
        if (thumbImage.isDefaultConfig()) {
            thumbImage.setDefaultSize(this.thumbImageConfig.getWidth(), this.thumbImageConfig.getHeight());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Thumbnails.of(new InputStream[]{inputStream}).size(this.thumbImageConfig.getWidth(), this.thumbImageConfig.getHeight()).imageType(2).toOutputStream(out);
            return new ByteArrayInputStream(out.toByteArray());
        } else {
            throw new MyException(500,"未支持自定义尺寸");
        }
    }

}
