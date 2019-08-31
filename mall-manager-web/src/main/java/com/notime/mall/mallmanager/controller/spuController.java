/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: spuController
 * Author:   Administrator
 * Date:     2019/8/28 19:50
 * Description: 商品属性spu属性管理
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.notime.mall.mallmanager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.notime.mall.api.bean.PmsProductImage;
import com.notime.mall.api.bean.PmsProductInfo;
import com.notime.mall.api.bean.PmsProductSaleAttr;
import com.notime.mall.api.service.PmsProductImageService;
import com.notime.mall.api.service.PmsProductInfoService;
import com.notime.mall.api.service.PmsProductSaleAttrService;
import com.notime.mall.mallmanager.util.MyFileUploadUtil;
import org.csource.common.MyException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class spuController {
    //http://127.0.0.1:8081/spuList?catalog3Id=61
    //PmsProductInfo

    @Reference
    PmsProductInfoService pmsProductInfoService;
    @CrossOrigin
    @RequestMapping("spuList")
    public List<PmsProductInfo> getSpuListBycatalog3Id(String catalog3Id){
        List<PmsProductInfo> pmsProductInfoList= pmsProductInfoService.getSpuListBycatalog3Id(catalog3Id);

        return pmsProductInfoList;
    }

    //http://127.0.0.1:8081/saveSpuInfo
    @CrossOrigin
    @RequestMapping("saveSpuInfo")
    public String saveSpuInfo(@RequestBody PmsProductInfo pmsProductInfo) {
        pmsProductInfoService.saveSpuInfo(pmsProductInfo);
        return  "success";
    }

    //http://127.0.0.1:8081/spuSaleAttrList?spuId=24
    // 添加sku  显示每个销售属性的具体值
    // 三个表  info attr value
    ////http://127.0.0.1:8081/spuSaleAttrList?spuId=80

    @Reference
    PmsProductSaleAttrService pmsProductSaleAttrService;
    @CrossOrigin
    @RequestMapping("spuSaleAttrList")
    public List<PmsProductSaleAttr> getspuSaleAttrListByspuId(String  spuId) {
        List<PmsProductSaleAttr> pmsProductSaleAttrList =  pmsProductSaleAttrService.getspuSaleAttrListByspuId(spuId);
        return  pmsProductSaleAttrList;
    }

  //  http://127.0.0.1:8081/fileUpload  图片上传

    @Reference
    PmsProductImageService PmsProductImageService;
    @CrossOrigin
    @RequestMapping("fileUpload")
    public String fileUpload(@RequestParam("file") MultipartFile multipartFile) {

            // 文件上传默认 file
        String url = null;
        try {
            url = MyFileUploadUtil.uploadImage(multipartFile);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
        // 返回url 为保存spu sku准备


        return  url;    // 这个Url 有没有讲究的 bean属性是imgUrl
    }

    //http://127.0.0.1:8081/spuImageList?spuId=68

    @CrossOrigin
    @RequestMapping("spuImageList")
    public List<PmsProductImage> getspuImageListBySpuId(@RequestParam("spuId") String spuId) {

       return   PmsProductImageService.getspuImageListBySpuId(spuId);


    }
}
