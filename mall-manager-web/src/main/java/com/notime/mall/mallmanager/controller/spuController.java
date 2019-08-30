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
import com.notime.mall.api.bean.PmsProductInfo;
import com.notime.mall.api.bean.PmsProductSaleAttr;
import com.notime.mall.api.service.PmsProductInfoService;
import com.notime.mall.api.service.PmsProductSaleAttrService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    //

    @Reference
    PmsProductSaleAttrService pmsProductSaleAttrService;
    @CrossOrigin
    @RequestMapping("spuSaleAttrList")
    public List<PmsProductSaleAttr> getspuSaleAttrListByspuId(String  spuId) {
        List<PmsProductSaleAttr> pmsProductSaleAttrList =  pmsProductSaleAttrService.getspuSaleAttrListByspuId(spuId);
        return  pmsProductSaleAttrList;
    }
}
