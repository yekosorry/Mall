package com.notime.mall.mallmanager;

import org.csource.common.MyException;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

import java.io.IOException;

public class UploadTest {
    public static void main(String[] artgs) throws IOException, MyException {
        // 测试上传图片


        // 类加载器 读取配置文件


        // 获取tracker
        // storage
        //上传文件
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();


        StorageClient storageClient = new StorageClient(trackerServer, null);
        String[] jpgs = storageClient.upload_appender_file("F:/wallhaven-48yeko.jpg", "jpg", null);
        for (String jpg : jpgs) {
            System.err.println(jpg);
        }
    }
}