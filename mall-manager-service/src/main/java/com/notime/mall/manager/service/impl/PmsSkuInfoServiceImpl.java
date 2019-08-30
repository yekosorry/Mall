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
import com.notime.mall.api.bean.PmsSkuInfo;
import com.notime.mall.api.service.PmsSkuInfoService;
import com.notime.mall.manager.mapper.PmsSkuInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class PmsSkuInfoServiceImpl implements PmsSkuInfoService {


    @Autowired
    PmsSkuInfoMapper pmsSkuInfoMapper;
    @Override
    public void saveSkuInfo(PmsSkuInfo pmsSkuInfo) {



    }
}
