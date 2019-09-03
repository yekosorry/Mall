package com.notime.mall.manager.mapper;

import com.notime.mall.api.bean.PmsProductSaleAttr;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface PmsProductSaleAttrMapper  extends Mapper<PmsProductSaleAttr> {
    List<PmsProductSaleAttr> getspuSaleAttrListByspuId(String spuId);

    List<PmsProductSaleAttr> getSpuSaleAttrListByProductId(@Param("productId") String productId,@Param("skuId") String skuId);
}
