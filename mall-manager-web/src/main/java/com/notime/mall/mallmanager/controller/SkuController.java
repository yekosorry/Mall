/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: SkuController
 * Author:   Administrator
 * Date:     2019/8/30 13:14
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.notime.mall.mallmanager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.notime.mall.api.bean.PmsSkuInfo;
import com.notime.mall.api.service.PmsSkuInfoService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SkuController {
    //saveSkuInfo
    //PmsSkuInfo

    @Reference
    PmsSkuInfoService pmsSkuInfoService;
    @RequestMapping("saveSkuInfo")
    public String  saveSkuInfo(@RequestBody PmsSkuInfo pmsSkuInfo){
        pmsSkuInfoService.saveSkuInfo(pmsSkuInfo);
        return "success";
    }
}
