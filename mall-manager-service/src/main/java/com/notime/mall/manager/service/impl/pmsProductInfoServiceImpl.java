/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: pmsProductInfoServiceImpl
 * Author:   Administrator
 * Date:     2019/8/28 20:15
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.notime.mall.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.notime.mall.api.bean.PmsProductInfo;
import com.notime.mall.api.bean.PmsProductSaleAttr;
import com.notime.mall.api.bean.PmsProductSaleAttrValue;
import com.notime.mall.api.service.PmsProductInfoService;
import com.notime.mall.manager.mapper.PmsProductInfoMapper;
import com.notime.mall.manager.mapper.PmsProductSaleAttrMapper;
import com.notime.mall.manager.mapper.PmsProductSaleAttrValueMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class pmsProductInfoServiceImpl implements PmsProductInfoService {

    @Autowired
    PmsProductInfoMapper pmsProductInfoMapper;
    @Override
    public List<PmsProductInfo> getSpuListBycatalog3Id(String catalog3Id) {
        PmsProductInfo pmsProductInfo = new PmsProductInfo();
        pmsProductInfo.setCatalog3Id(catalog3Id);
        List<PmsProductInfo> pmsProductInfoList = pmsProductInfoMapper.select(pmsProductInfo);
        return pmsProductInfoList;
    }

    @Autowired
    PmsProductSaleAttrMapper pmsProductSaleAttrMapper;

    @Autowired
    PmsProductSaleAttrValueMapper pmsProductSaleAttrValueMapper;
    @Override
    public void saveSpuInfo(PmsProductInfo pmsProductInfo) {

        pmsProductInfoMapper.insertSelective(pmsProductInfo);

        //  还需要添加private List<PmsProductSaleAttr> spuSaleAttrList;  对应 一张表

        // PmsProductSaleAttr 中还有 List<PmsProductSaleAttrValue> spuSaleAttrValueList;  对应 一张表

        // 两个表的插入需要设置逐渐插入 直接获取的是Null
        //id='null', productId='null', saleAttrId='4', saleAttrName='容量'

//        List<PmsProductSaleAttr> pmsProductInfoSpuSaleAttrList = pmsProductInfo.getSpuSaleAttrList();
//        System.err.println(pmsProductInfoSpuSaleAttrList);
//        for (PmsProductSaleAttr pmsProductSaleAttr : pmsProductInfoSpuSaleAttrList) {
//
//
//            pmsProductSaleAttrMapper.insertSelective(pmsProductSaleAttr);
//        }
        List<PmsProductSaleAttr> pmsProductInfoSpuSaleAttrList = pmsProductInfo.getSpuSaleAttrList();
        String id = pmsProductInfo.getId();  // 就是productId

        for (PmsProductSaleAttr pmsProductSaleAttr : pmsProductInfoSpuSaleAttrList) {

            pmsProductSaleAttr.setProductId(id);
            pmsProductSaleAttrMapper.insertSelective(pmsProductSaleAttr);
            List<PmsProductSaleAttrValue> spuSaleAttrValueList = pmsProductSaleAttr.getSpuSaleAttrValueList();
            for (PmsProductSaleAttrValue pmsProductSaleAttrValue : spuSaleAttrValueList) {
                pmsProductSaleAttrValue.setProductId(id);
                pmsProductSaleAttrValueMapper.insertSelective(pmsProductSaleAttrValue);
            }

        }
    }
}
