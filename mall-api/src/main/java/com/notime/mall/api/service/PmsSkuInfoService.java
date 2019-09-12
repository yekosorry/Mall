/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: PmsSkuInfoService
 * Author:   Administrator
 * Date:     2019/8/30 13:17
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.notime.mall.api.service;

import com.notime.mall.api.bean.PmsSkuInfo;

import java.util.List;

public interface PmsSkuInfoService {
    void saveSkuInfo(PmsSkuInfo pmsSkuInfo);

    PmsSkuInfo getSkuInfoBySkuId(String skuId) throws InterruptedException;

    List<PmsSkuInfo> getSkuInfoByProductId(String productId);

    List<PmsSkuInfo> getAllSkuInfo();

    PmsSkuInfo getSkuInfoById(String productSkuId);
}
