package com.project.config.fastdfs;

import com.github.tobato.fastdfs.domain.conn.FdfsConnectionManager;
import com.github.tobato.fastdfs.domain.fdfs.FileInfo;
import com.github.tobato.fastdfs.domain.fdfs.MetaData;
import com.github.tobato.fastdfs.domain.fdfs.StorageNode;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadCallback;
import com.github.tobato.fastdfs.domain.proto.storage.StorageUploadFileCommand;
import com.github.tobato.fastdfs.domain.upload.FastFile;
import com.github.tobato.fastdfs.domain.upload.FastImageFile;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Set;
@Service("devupload")
@ConditionalOnExpression("environment['spring.profiles.active'].equals('dev')")
public class FastDfsuploadClient implements FastFileStorageClient {
    @Autowired
    protected FdfsConnectionManager fdfsConnectionManager;

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
        return null;
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

    }

    @Override
    public <T> T downloadFile(String groupName, String path, DownloadCallback<T> callback) {
        return null;
    }

    @Override
    public <T> T downloadFile(String groupName, String path, long fileOffset, long fileSize, DownloadCallback<T> callback) {
        return null;
    }
}
