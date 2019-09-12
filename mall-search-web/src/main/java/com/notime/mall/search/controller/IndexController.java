package com.notime.mall.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.*;
import com.notime.mall.api.bean.PmsBaseCatalog1;
import com.notime.mall.api.service.PmsBaseCatalog1Service;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Controller
public class IndexController {

    // 测试跳转首页
    @RequestMapping("index")
    public  String  index(){

        return "index";
    }

    @Reference
    PmsBaseCatalog1Service pmsBaseCatalog1Service;
    // 生成自己的catalog1.json

    @RequestMapping("getcatalog")
    @ResponseBody
    public  String getcatalog() throws IOException {

        File file = new File("d:/catalog.json");

        FileOutputStream fileOutputStream = new FileOutputStream(file);
        List<PmsBaseCatalog1> pmsBaseCatalog1ServiceAll = pmsBaseCatalog1Service.getAll();
        System.err.println(pmsBaseCatalog1ServiceAll);
        PmsBaseCatalog1 pmsBaseCatalogjson = null;

       String toJSONString = JSON.toJSONString(pmsBaseCatalog1ServiceAll);

        System.err.println("json"+toJSONString);

        fileOutputStream.write(JSON.toJSONString(pmsBaseCatalog1ServiceAll).getBytes());
        return toJSONString;
    }
}
