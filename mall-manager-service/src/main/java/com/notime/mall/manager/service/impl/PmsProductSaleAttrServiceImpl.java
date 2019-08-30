/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: PmsProductSaleAttrServiceImpl
 * Author:   Administrator
 * Date:     2019/8/29 15:37
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.notime.mall.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.notime.mall.api.bean.PmsProductSaleAttr;
import com.notime.mall.api.bean.PmsProductSaleAttrValue;
import com.notime.mall.api.service.PmsProductSaleAttrService;
import com.notime.mall.manager.mapper.PmsProductSaleAttrMapper;
import com.notime.mall.manager.mapper.PmsProductSaleAttrValueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class PmsProductSaleAttrServiceImpl implements PmsProductSaleAttrService {

   @Autowired
    PmsProductSaleAttrMapper pmsProductSaleAttrMapper;

   @Autowired
    PmsProductSaleAttrValueMapper pmsProductSaleAttrValueMapper;
    @Override
    public List<PmsProductSaleAttr> getspuSaleAttrListByspuId(String spuId) {


      //  return pmsProductSaleAttrMapper.getspuSaleAttrListByspuId(spuId);  数据格式老是对不上 传多了不可以不要吗
        // sql 语句写错了 pmsProductSaleAttrValue  要求 setProductId setSaleAttrId  都要与pmsProductSaleAttr 相等
        Example example = new Example(PmsProductSaleAttr.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productId",spuId);
        List<PmsProductSaleAttr> attrsList = pmsProductSaleAttrMapper.selectByExample(example);
        PmsProductSaleAttrValue pmsProductSaleAttrValue = new PmsProductSaleAttrValue();
        for (PmsProductSaleAttr pmsProductSaleAttr : attrsList) {

            pmsProductSaleAttrValue.setProductId(spuId);
            pmsProductSaleAttrValue.setSaleAttrId(pmsProductSaleAttr.getSaleAttrId());
            List<PmsProductSaleAttrValue> pmsProductSaleAttrValueList = pmsProductSaleAttrValueMapper.select(pmsProductSaleAttrValue);
            pmsProductSaleAttr.setSpuSaleAttrValueList(pmsProductSaleAttrValueList);

        }
     // return attrsList;
        List<PmsProductSaleAttr> pmsProductSaleAttrList = pmsProductSaleAttrMapper.getspuSaleAttrListByspuId(spuId);
        return pmsProductSaleAttrList;
    }
}
