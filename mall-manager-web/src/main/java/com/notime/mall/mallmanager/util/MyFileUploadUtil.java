package com.notime.mall.mallmanager.util;
import com.notime.mall.constanst.MyContanst;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;

import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class MyFileUploadUtil {


    public static String uploadImage(MultipartFile multipartFile) throws IOException, MyException {

        String path = MyFileUploadUtil.class.getClassLoader().getResource("tracker.conf").getPath();

        ClientGlobal.init(path);

        // tracker  storage
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();

        StorageClient storageClient = new StorageClient();

        String originalFilename = multipartFile.getOriginalFilename();
        NameValuePair[] metaList = null;
        byte[] fileBuff = multipartFile.getBytes();
        int i = originalFilename.lastIndexOf(".");

        String fileExtName = originalFilename.substring(i+1);
        String[] uploadFile = storageClient.upload_file(fileBuff, fileExtName, metaList);

        String url =  MyContanst.URL;
        for (String s : uploadFile) {
            url = url+ "/"+s;
        }

        return  url;
    }
}
