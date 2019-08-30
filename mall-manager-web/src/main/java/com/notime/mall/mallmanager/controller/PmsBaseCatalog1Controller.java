/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: PmsBaseCatalog1Controller
 * Author:   Administrator
 * Date:     2019/8/28 13:25
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.notime.mall.mallmanager.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.notime.mall.api.bean.PmsBaseCatalog1;
import com.notime.mall.api.bean.PmsBaseCatalog2;
import com.notime.mall.api.bean.PmsBaseCatalog3;
import com.notime.mall.api.service.PmsBaseCatalog1Service;
import com.notime.mall.api.service.PmsBaseCatalog2Service;
import com.notime.mall.api.service.PmsBaseCatalog3Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PmsBaseCatalog1Controller {

    //http://192.168.93.100:8081/getCatalog1

    @Reference
    private PmsBaseCatalog1Service pmsBaseCatalog1Service;

    @CrossOrigin
    @RequestMapping("getCatalog1")
    public List<PmsBaseCatalog1> getCatalog1(){
        List<PmsBaseCatalog1> allPmsBaseCatalog1=  pmsBaseCatalog1Service.getAll();
        return allPmsBaseCatalog1;
    }

    //http://127.0.0.1:8081/getCatalog2?catalog1Id=5

    @Reference
    private PmsBaseCatalog2Service pmsBaseCatalog2Service;
    @CrossOrigin
    @RequestMapping("getCatalog2")
    public List<PmsBaseCatalog2> getCatalog2ByCatalogId(String catalog1Id ){
        List<PmsBaseCatalog2> allPmsBaseCatalog2=  pmsBaseCatalog2Service.getCatalog2ByCatalogId(catalog1Id);
        return allPmsBaseCatalog2;
    }


    //http://127.0.0.1:8081/getCatalog3?catalog2Id=13

    @Reference
    private PmsBaseCatalog3Service pmsBaseCatalog3Service;
    @CrossOrigin
    @RequestMapping("getCatalog3")
    public List<PmsBaseCatalog3> getCatalog3ByCatalog2Id(String catalog2Id ){
        List<PmsBaseCatalog3> allPmsBaseCatalog3=  pmsBaseCatalog3Service.getCatalog3ByCatalog2Id(catalog2Id);
        return allPmsBaseCatalog3;
    }



    //http://127.0.0.1:8081/attrInfoList?catalog3Id=414
    // PmsBaseAttrInfo
}
