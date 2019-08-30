/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: PmsBaseAttrValueServiceImpl
 * Author:   Administrator
 * Date:     2019/8/28 22:21
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.notime.mall.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.notime.mall.api.bean.PmsBaseAttrValue;
import com.notime.mall.api.service.PmsBaseAttrValueService;
import com.notime.mall.manager.mapper.PmsBaseAttrValueMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Service
public class PmsBaseAttrValueServiceImpl implements PmsBaseAttrValueService {

    @Autowired
    PmsBaseAttrValueMapper pmsBaseAttrValueMapper;
    @Override
    public List<PmsBaseAttrValue> getAttrValueList(String attrId) {
        PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
        pmsBaseAttrValue.setAttrId(attrId);
        List<PmsBaseAttrValue> pmsBaseAttrValueList = pmsBaseAttrValueMapper.select(pmsBaseAttrValue);
        return pmsBaseAttrValueList;
    }
}
