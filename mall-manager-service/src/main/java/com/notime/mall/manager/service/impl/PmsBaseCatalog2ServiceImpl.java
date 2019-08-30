/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: PmsBaseCatalog2ServiceImpl
 * Author:   Administrator
 * Date:     2019/8/28 13:55
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.notime.mall.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.notime.mall.api.bean.PmsBaseCatalog2;
import com.notime.mall.api.service.PmsBaseCatalog2Service;
import com.notime.mall.manager.mapper.PmsBaseCatalog2Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Service
public class PmsBaseCatalog2ServiceImpl implements PmsBaseCatalog2Service {

    @Autowired
    PmsBaseCatalog2Mapper pmsBaseCatalog2Mapper;
    @Override
    public List<PmsBaseCatalog2> getCatalog2ByCatalogId(String catalog1Id) {
//        PmsBaseCatalog1 pmsBaseCatalog1 = new PmsBaseCatalog1();
//        pmsBaseCatalog1.setId(catalog1Id);
//        List<PmsBaseCatalog2> pmsBaseCatalog2List = pmsBaseCatalog2Mapper.select(pmsBaseCatalog1);

        PmsBaseCatalog2 pmsBaseCatalog2 = new PmsBaseCatalog2();
        pmsBaseCatalog2.setCatalog1Id(catalog1Id);
        List<PmsBaseCatalog2> pmsBaseCatalog2List = pmsBaseCatalog2Mapper.select(pmsBaseCatalog2);
        return pmsBaseCatalog2List;

    }
}
