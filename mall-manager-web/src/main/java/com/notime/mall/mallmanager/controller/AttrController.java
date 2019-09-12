/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: AttrController
 * Author:   Administrator
 * Date:     2019/8/28 18:30
 * Description: 对平台属性的管理
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.notime.mall.mallmanager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.notime.mall.api.bean.PmsBaseAttrInfo;
import com.notime.mall.api.bean.PmsBaseAttrValue;
import com.notime.mall.api.service.PmsBaseAttrInfoService;
import com.notime.mall.api.service.PmsBaseAttrValueService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AttrController {
    //http://127.0.0.1:8081/attrInfoList?catalog3Id=414
    //PmsBaseAttrInfo

    @Reference
    private PmsBaseAttrInfoService pmsBaseAttrInfoService;
    @CrossOrigin
    @RequestMapping("attrInfoList")
    public List<PmsBaseAttrInfo> getAttrInfoList(String catalog3Id){
        List<PmsBaseAttrInfo>  pmsBaseAttrInfoList = pmsBaseAttrInfoService.getAttrInfoList(catalog3Id);

        return pmsBaseAttrInfoList;
    }

    //http://127.0.0.1:8081/saveAttrInfo
    @CrossOrigin
    @RequestMapping("saveAttrInfo")
    public String saveAttrInfo(@RequestBody  PmsBaseAttrInfo pmsBaseAttrInfo){
        System.err.println(pmsBaseAttrInfo);

        pmsBaseAttrInfoService.saveAttrInfo(pmsBaseAttrInfo);

        return  "success";
    }

    //http://127.0.0.1:8081/saveAttrInfo
    //http://127.0.0.1:8081/getAttrValueList?attrId=47
    // 修改前需要显示原来的值

    @Reference
    PmsBaseAttrValueService pmsBaseAttrValueService;
    @CrossOrigin
    @RequestMapping("getAttrValueList")
    public  List<PmsBaseAttrValue> getAttrValueList(String  attrId){
        List<PmsBaseAttrValue> pmsBaseAttrValueList = pmsBaseAttrValueService.getAttrValueList(attrId);
//PmsBaseAttrValue
        return  pmsBaseAttrValueList;
    }

    

}