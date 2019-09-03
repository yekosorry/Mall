package com.notime.mall.manager.mapper;

import com.notime.mall.api.bean.PmsSkuSaleAttrValue;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface PmsSkuSaleAttrValueMapper  extends Mapper<PmsSkuSaleAttrValue> {




    List<PmsSkuSaleAttrValue> getSkuSaleAttrValueListBySpu(@Param("productId") String productId);
}
