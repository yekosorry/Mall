package com.notime.mall.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.notime.mall.api.bean.PmsSkuSaleAttrValue;
import com.notime.mall.api.service.PmsSkuSaleAttrValueService;
import com.notime.mall.manager.mapper.PmsSkuSaleAttrValueMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class PmsSkuSaleAttrValueServiceImpl  implements PmsSkuSaleAttrValueService {

    @Autowired
    PmsSkuSaleAttrValueMapper pmsSkuSaleAttrValueMapper;

    @Override
    public List<PmsSkuSaleAttrValue> getSkuSaleAttrValueListBySpu(String productId) {

       return  pmsSkuSaleAttrValueMapper.getSkuSaleAttrValueListBySpu(productId);
    }
}
