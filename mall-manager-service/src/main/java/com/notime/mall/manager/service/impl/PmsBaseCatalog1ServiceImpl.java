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
import com.notime.mall.api.service.PmsBaseCatalog1Service;
import com.notime.mall.manager.mapper.PmsBaseCatalog1Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class PmsBaseCatalog1ServiceImpl implements PmsBaseCatalog1Service {

    @Autowired
    PmsBaseCatalog1Mapper pmsBaseCatalog1Mapper;
    @Override
    public List<PmsBaseCatalog1> getAll() {
        List<PmsBaseCatalog1> pmsBaseCatalog1List = pmsBaseCatalog1Mapper.selectAll();
        return pmsBaseCatalog1List;
    }
}
