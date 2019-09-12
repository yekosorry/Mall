/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: PmsBaseCatalog1ServiceImpl
 * Author:   Administrator
 * Date:     2019/8/28 13:34
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.notime.mall.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.notime.mall.api.bean.PmsBaseCatalog1;
import com.notime.mall.api.bean.PmsBaseCatalog2;
import com.notime.mall.api.bean.PmsBaseCatalog3;
import com.notime.mall.api.service.PmsBaseCatalog1Service;
import com.notime.mall.manager.mapper.PmsBaseCatalog1Mapper;
import com.notime.mall.manager.mapper.PmsBaseCatalog2Mapper;
import com.notime.mall.manager.mapper.PmsBaseCatalog3Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class PmsBaseCatalog1ServiceImpl implements PmsBaseCatalog1Service {

    @Autowired
    PmsBaseCatalog1Mapper pmsBaseCatalog1Mapper;
    @Autowired
    PmsBaseCatalog2Mapper pmsBaseCatalog2Mapper;
    @Autowired
    PmsBaseCatalog3Mapper pmsBaseCatalog3Mapper;
    @Override
    public List<PmsBaseCatalog1> getAll() {
        List<PmsBaseCatalog1> pmsBaseCatalog1List = pmsBaseCatalog1Mapper.selectAll();
        for (PmsBaseCatalog1 pmsBaseCatalog1 : pmsBaseCatalog1List) {

            String catalog1Id = pmsBaseCatalog1.getId();
            PmsBaseCatalog2 pmsBaseCatalog2 = new PmsBaseCatalog2();
            pmsBaseCatalog2.setCatalog1Id(catalog1Id);
            List<PmsBaseCatalog2> pmsBaseCatalog2List = pmsBaseCatalog2Mapper.select(pmsBaseCatalog2);
            for (PmsBaseCatalog2 baseCatalog2 : pmsBaseCatalog2List) {
                String catalog2Id = baseCatalog2.getId();
                PmsBaseCatalog3 pmsBaseCatalog3 = new PmsBaseCatalog3();
                pmsBaseCatalog3.setCatalog2Id(catalog2Id);
                List<PmsBaseCatalog3> pmsBaseCatalog3List = pmsBaseCatalog3Mapper.select(pmsBaseCatalog3);
                baseCatalog2.setCatalog3List(pmsBaseCatalog3List);
            }

            pmsBaseCatalog1.setCatalog2s(pmsBaseCatalog2List);
        }
        return pmsBaseCatalog1List;
    }


}
