/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: PmsProductInfoService
 * Author:   Administrator
 * Date:     2019/8/28 20:15
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.notime.mall.api.service;

import com.notime.mall.api.bean.PmsProductInfo;
import com.notime.mall.api.bean.PmsProductSaleAttr;

import java.util.List;

public interface PmsProductInfoService {
    List<PmsProductInfo> getSpuListBycatalog3Id(String catalog3Id);

    void saveSpuInfo(PmsProductInfo pmsProductInfo);

    List<PmsProductSaleAttr> getSpuSaleAttrListByProductId(String productId, String skuId);
}
