/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: PmsBaseSaleAttrServierImpl
 * Author:   Administrator
 * Date:     2019/8/28 21:05
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.notime.mall.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.notime.mall.api.bean.PmsBaseSaleAttr;
import com.notime.mall.api.service.PmsBaseSaleAttrServier;
import com.notime.mall.manager.mapper.PmsBaseSaleAttrMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Service
public class PmsBaseSaleAttrServierImpl implements PmsBaseSaleAttrServier {
    @Autowired
    PmsBaseSaleAttrMapper pmsBaseSaleAttrMapper;
    @Override
    public List<PmsBaseSaleAttr> getBaseSaleAttrList() {
        List<PmsBaseSaleAttr> pmsBaseSaleAttrs = pmsBaseSaleAttrMapper.selectAll();
        return pmsBaseSaleAttrs;

    }
}
