/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: PmsBaseAttrInfoServiceImpl
 * Author:   Administrator
 * Date:     2019/8/28 18:34
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.notime.mall.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.notime.mall.api.bean.PmsBaseAttrInfo;
import com.notime.mall.api.bean.PmsBaseAttrValue;
import com.notime.mall.api.service.PmsBaseAttrInfoService;
import com.notime.mall.manager.mapper.PmsBaseAttrInfoMapper;
import com.notime.mall.manager.mapper.PmsBaseAttrValueMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class PmsBaseAttrInfoServiceImpl implements PmsBaseAttrInfoService {

    @Autowired
    PmsBaseAttrInfoMapper pmsBaseAttrInfoMapper;


    @Autowired
    PmsBaseAttrValueMapper pmsBaseAttrValueMapper;
    @Override

    public List<PmsBaseAttrInfo> getAttrInfoList(String catalog3Id) {
        PmsBaseAttrInfo pmsBaseAttrInfo = new PmsBaseAttrInfo();
        pmsBaseAttrInfo.setCatalog3Id(catalog3Id);
        List<PmsBaseAttrInfo> pmsBaseAttrInfoList = pmsBaseAttrInfoMapper.select(pmsBaseAttrInfo);

        //    @Transient
        //    List<PmsBaseAttrValue> attrValueList;
        for (PmsBaseAttrInfo baseAttrInfo : pmsBaseAttrInfoList) {
            PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
            String id = baseAttrInfo.getId();
            pmsBaseAttrValue.setAttrId(id);
            List<PmsBaseAttrValue> select = pmsBaseAttrValueMapper.select(pmsBaseAttrValue);
            baseAttrInfo.setAttrValueList(select);
        }


        return pmsBaseAttrInfoList;
    }

    @Override
    public void saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo) {
        // 保存与新增都是同一个方法名 需要判断
        String id = pmsBaseAttrInfo.getId();
        if (id == null){
            pmsBaseAttrInfoMapper.insertSelective(pmsBaseAttrInfo);

            //PmsBaseAttrInfo{id='12', attrName='尺寸', catalog3Id='61', isEnabled='null', attrValueList=[com.notime.mall.api.bean.PmsBaseAttrValue@43139541], attrId='12'}
            // 保存到数据库的只有id='12', attrName='尺寸', catalog3Id='61',  是pms-base-attr -info
            // 保存后面的数据 需要 pms-base-attr -value
            List<PmsBaseAttrValue> baseAttrInfoAttrValueList = pmsBaseAttrInfo.getAttrValueList();
            for (PmsBaseAttrValue pmsBaseAttrValue : baseAttrInfoAttrValueList) {
                pmsBaseAttrValue.setAttrId(id);
                pmsBaseAttrValueMapper.insertSelective(pmsBaseAttrValue);
            }
        }

        pmsBaseAttrInfoMapper.updateByPrimaryKeySelective(pmsBaseAttrInfo);

        List<PmsBaseAttrValue> baseAttrInfoAttrValueList = pmsBaseAttrInfo.getAttrValueList();

        //  pmsBaseAttrValueMapper.updateByPrimaryKeySelective(pmsBaseAttrValue);  一直存在 update没有用
        // 全删了 加过
        // 有多少个  是artId下的pmsBaseAttrValue   == id  pmsBaseAttrInfo
        PmsBaseAttrValue pmsBaseAttrValue1 = new PmsBaseAttrValue();
        pmsBaseAttrValue1.setAttrId(id);

        List<PmsBaseAttrValue> pmsBaseAttrValueList = pmsBaseAttrValueMapper.select(pmsBaseAttrValue1);
        for (PmsBaseAttrValue pmsBaseAttrValue : pmsBaseAttrValueList) {
            pmsBaseAttrValueMapper.delete(pmsBaseAttrValue);

        }
        for (PmsBaseAttrValue pmsBaseAttrValue : baseAttrInfoAttrValueList) {
            pmsBaseAttrValue.setAttrId(id);
            pmsBaseAttrValueMapper.insertSelective(pmsBaseAttrValue);
        }

    }

    @Override
    public PmsBaseAttrInfo getAttrValueList(String attrId) {
        PmsBaseAttrInfo pmsBaseAttrInfo = new PmsBaseAttrInfo();
         // 这样new 出来的id=null;
        pmsBaseAttrInfo.setId(attrId);
        PmsBaseAttrInfo pmsBaseAttrInfo1 = pmsBaseAttrInfoMapper.selectOne(pmsBaseAttrInfo);
        return pmsBaseAttrInfo1;
    }


}
