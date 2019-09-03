/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: pmsProductInfoServiceImpl
 * Author:   Administrator
 * Date:     2019/8/28 20:15
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.notime.mall.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.notime.mall.api.bean.*;
import com.notime.mall.api.service.PmsProductInfoService;
import com.notime.mall.manager.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class pmsProductInfoServiceImpl implements PmsProductInfoService {

    @Autowired
    PmsProductInfoMapper pmsProductInfoMapper;

    @Autowired
    PmsProductSaleAttrMapper pmsProductSaleAttrMapper;

    @Autowired
    PmsProductImageMapper pmsProductImageMapper;
    @Override
    public List<PmsProductInfo> getSpuListBycatalog3Id(String catalog3Id) {
        PmsProductInfo pmsProductInfo = new PmsProductInfo();
        pmsProductInfo.setCatalog3Id(catalog3Id);
        List<PmsProductInfo> pmsProductInfoList = pmsProductInfoMapper.select(pmsProductInfo);

        for (PmsProductInfo productInfo : pmsProductInfoList) {
            PmsProductSaleAttr pmsProductSaleAttr = new PmsProductSaleAttr();
            String id = productInfo.getId();
            pmsProductSaleAttr.setProductId(id);
            List<PmsProductSaleAttr> pmsProductSaleAttrList = pmsProductSaleAttrMapper.select(pmsProductSaleAttr);
            productInfo.setSpuSaleAttrList(pmsProductSaleAttrList);


            PmsProductImage pmsProductImage = new PmsProductImage();
            pmsProductImage.setProductId(id);
            List<PmsProductImage> pmsProductImageList = pmsProductImageMapper.select(pmsProductImage);
            productInfo.setSpuImageList(pmsProductImageList);
        }
        return pmsProductInfoList;
        /*
          @Transient
    private List<PmsProductSaleAttr> spuSaleAttrList;



    @Transient
    private List<PmsProductImage> spuImageList;
         */

    }


    @Autowired
    PmsProductSaleAttrValueMapper pmsProductSaleAttrValueMapper;
    @Override
    public void saveSpuInfo(PmsProductInfo pmsProductInfo) {

        pmsProductInfoMapper.insertSelective(pmsProductInfo);

        //  还需要添加private List<PmsProductSaleAttr> spuSaleAttrList;  对应 一张表

        // PmsProductSaleAttr 中还有 List<PmsProductSaleAttrValue> spuSaleAttrValueList;  对应 一张表

        // 两个表的插入需要设置逐渐插入 直接获取的是Null
        //id='null', productId='null', saleAttrId='4', saleAttrName='容量'

//        List<PmsProductSaleAttr> pmsProductInfoSpuSaleAttrList = pmsProductInfo.getSpuSaleAttrList();
//        System.err.println(pmsProductInfoSpuSaleAttrList);
//        for (PmsProductSaleAttr pmsProductSaleAttr : pmsProductInfoSpuSaleAttrList) {
//
//
//            pmsProductSaleAttrMapper.insertSelective(pmsProductSaleAttr);
//        }
        List<PmsProductSaleAttr> pmsProductInfoSpuSaleAttrList = pmsProductInfo.getSpuSaleAttrList();
        String id = pmsProductInfo.getId();  // 就是productId

        for (PmsProductSaleAttr pmsProductSaleAttr : pmsProductInfoSpuSaleAttrList) {

            pmsProductSaleAttr.setProductId(id);
            pmsProductSaleAttrMapper.insertSelective(pmsProductSaleAttr);
            List<PmsProductSaleAttrValue> spuSaleAttrValueList = pmsProductSaleAttr.getSpuSaleAttrValueList();
            for (PmsProductSaleAttrValue pmsProductSaleAttrValue : spuSaleAttrValueList) {
                pmsProductSaleAttrValue.setProductId(id);
                pmsProductSaleAttrValueMapper.insertSelective(pmsProductSaleAttrValue);
            }

        }

        // 保存图片
           // product 就是 PmsProductInfo的id
        PmsProductImage pmsProductImage = new PmsProductImage();
        pmsProductImage.setProductId(id);
        List<PmsProductImage> spuImageList = pmsProductInfo.getSpuImageList();
        for (PmsProductImage productImage : spuImageList) {

            /*
              @Column
    @Id
    private String id;
    @Column
    private String productId;
    @Column
    private String imgName;
    @Column
    private String imgUrl;

            四个属性都是数据库存在的字段 不能直接获取吗 还要设Productid

            // 这个需要分析前端代码吧
             */
            productImage.setProductId(id);
            pmsProductImageMapper.insertSelective(productImage);
        }
    }

    @Override
    public List<PmsProductSaleAttr> getSpuSaleAttrListByProductId(String productId, String skuId) {

       return   pmsProductSaleAttrMapper.getSpuSaleAttrListByProductId(productId,skuId);

    }
}
