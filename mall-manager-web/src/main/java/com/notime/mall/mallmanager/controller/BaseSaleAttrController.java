/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: BaseSaleAttrController
 * Author:   Administrator
 * Date:     2019/8/28 21:02
 * Description: 基本销售属性
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.notime.mall.mallmanager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.notime.mall.api.bean.PmsBaseSaleAttr;
import com.notime.mall.api.service.PmsBaseSaleAttrServier;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BaseSaleAttrController {

    //http://127.0.0.1:8081/baseSaleAttrList
    //PmsBaseSaleAttr

    @Reference
    PmsBaseSaleAttrServier pmsBaseSaleAttrServier;
    @RequestMapping("baseSaleAttrList")
    @CrossOrigin
    public List<PmsBaseSaleAttr>  getBaseSaleAttrList(){
        List<PmsBaseSaleAttr> PmsBaseSaleAttr =pmsBaseSaleAttrServier.getBaseSaleAttrList();
        return  PmsBaseSaleAttr;
    }

}
