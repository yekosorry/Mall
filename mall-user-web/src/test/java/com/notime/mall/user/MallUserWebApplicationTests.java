package com.notime.mall.user;

import org.csource.fastdfs.TrackerClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MallUserWebApplicationTests {

    @Test
    public void contextLoads() {

        // 测试文件上传

        // 获得tracker  连接
         //返回可用storage
        TrackerClient trackerClient = new TrackerClient();
        TrackerClient client = trackerClient;
    }

}
