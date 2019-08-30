/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: PmsBaseCatalog3ServiceImpl
 * Author:   Administrator
 * Date:     2019/8/28 14:06
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.notime.mall.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.notime.mall.api.bean.PmsBaseCatalog3;
import com.notime.mall.api.service.PmsBaseCatalog3Service;
import com.notime.mall.manager.mapper.PmsBaseCatalog3Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Service
public class PmsBaseCatalog3ServiceImpl implements PmsBaseCatalog3Service {

    @Autowired
    PmsBaseCatalog3Mapper pmsBaseCatalog3Mapper;

    @Override
    public List<PmsBaseCatalog3> getCatalog3ByCatalog2Id(String catalog2Id) {

        PmsBaseCatalog3 pmsBaseCatalog3 = new PmsBaseCatalog3();
        pmsBaseCatalog3.setCatalog2Id(catalog2Id);
        List<PmsBaseCatalog3> pmsBaseCatalog3List = pmsBaseCatalog3Mapper.select(pmsBaseCatalog3);
        return  pmsBaseCatalog3List;
    }
}
