package com.notime.mall.api.service;

import com.notime.mall.api.bean.PmsProductSaleAttr;

import java.util.List;

public interface PmsProductSaleAttrService {
    List<PmsProductSaleAttr> getspuSaleAttrListByspuId(String spuId);

}
