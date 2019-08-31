/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: PmsSkuInfoServiceImpl
 * Author:   Administrator
 * Date:     2019/8/30 13:17
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.notime.mall.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.notime.mall.api.bean.PmsSkuAttrValue;
import com.notime.mall.api.bean.PmsSkuImage;
import com.notime.mall.api.bean.PmsSkuInfo;
import com.notime.mall.api.bean.PmsSkuSaleAttrValue;
import com.notime.mall.api.service.PmsSkuInfoService;
import com.notime.mall.manager.mapper.PmsSkuAttrValueMapper;
import com.notime.mall.manager.mapper.PmsSkuImageMapper;
import com.notime.mall.manager.mapper.PmsSkuInfoMapper;
import com.notime.mall.manager.mapper.PmsSkuSaleAttrValueMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class PmsSkuInfoServiceImpl implements PmsSkuInfoService {


    @Autowired
    PmsSkuInfoMapper pmsSkuInfoMapper;

    @Autowired
    PmsSkuImageMapper pmsSkuImageMapper;

    @Autowired
    PmsSkuAttrValueMapper pmsSkuAttrValueMapper;

    @Autowired
    PmsSkuSaleAttrValueMapper pmsSkuSaleAttrValueMapper;

    @Override
    public void saveSkuInfo(PmsSkuInfo pmsSkuInfo) {
        // PmsSkuInfo
        /// 还有pmsskuImageList    pmsSkuAttrValueList  PmsSkuAttrValueList
        //skuid
        pmsSkuInfoMapper.insertSelective(pmsSkuInfo);

        String id = pmsSkuInfo.getId();
        List<PmsSkuImage> skuImageList = pmsSkuInfo.getSkuImageList();
        for (PmsSkuImage pmsSkuImage : skuImageList) {
            pmsSkuImage.setSkuId(id);
            pmsSkuImageMapper.insertSelective(pmsSkuImage);
        }
        // 不了解数据结构 isImg怎么说 是设置的吗
        // pmsSkuInfo.getSkuDefaultImg().var

        List<PmsSkuAttrValue> skuAttrValueList = pmsSkuInfo.getSkuAttrValueList();
        for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
            pmsSkuAttrValue.setSkuId(id);
            pmsSkuAttrValueMapper.insert(pmsSkuAttrValue);
        }

        List<PmsSkuSaleAttrValue> skuSaleAttrValueList = pmsSkuInfo.getSkuSaleAttrValueList();

        for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : skuSaleAttrValueList) {
            pmsSkuSaleAttrValue.setSkuId(id);
            pmsSkuSaleAttrValueMapper.insert(pmsSkuSaleAttrValue);

        }
    }
}
