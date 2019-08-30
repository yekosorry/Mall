package com.notime.mall.manager.mapper;

import com.notime.mall.api.bean.PmsProductSaleAttr;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface PmsProductSaleAttrMapper  extends Mapper<PmsProductSaleAttr> {
    List<PmsProductSaleAttr> getspuSaleAttrListByspuId(String spuId);

}
