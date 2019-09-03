package com.notime.mall.manager.mapper;

import com.notime.mall.api.bean.PmsProductSaleAttr;
import com.notime.mall.api.bean.PmsProductSaleAttrValue;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface PmsProductSaleAttrValueMapper  extends Mapper<PmsProductSaleAttrValue> {

    List<PmsProductSaleAttr> getSpuSaleAttrListByProductId(@Param("productId") String productId, @Param("skuId") String skuId);

}
