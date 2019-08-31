package com.notime.mall.mallmanager;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MallManagerWebApplicationTests {

    @Test
    public void contextLoads() throws IOException, MyException {

       // 要先读取配置文件
        String path = MallManagerWebApplicationTests.class.getClassLoader().getResource("tracker.conf").getPath();
        ClientGlobal.init(path);
         // 测试上传图片

        // 获取tracker
        // storage
        //上传文件
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();

//http://192.168.93.100/group1/M00/00/00/wKhdZF1pHxSAdlXnACIPGv4u2h0504.jpg
        StorageClient storageClient = new StorageClient(trackerServer, null);
        String[] jpgs = storageClient.upload_file("F:/wallhaven-48yeko.jpg", "jpg", null);
        for (String jpg : jpgs) {
            System.err.println(jpg);
        }
    }



}
