package com.notime.mall.api.service;

import com.notime.mall.api.bean.PmsSkuSaleAttrValue;

import java.util.List;

public interface PmsSkuSaleAttrValueService {
    List<PmsSkuSaleAttrValue> getSkuSaleAttrValueListBySpu(String productId);
}
