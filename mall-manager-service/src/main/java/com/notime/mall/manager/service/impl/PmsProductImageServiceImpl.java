package com.notime.mall.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.notime.mall.api.bean.PmsProductImage;
import com.notime.mall.api.service.PmsProductImageService;
import com.notime.mall.manager.mapper.PmsProductImageMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Service
public class PmsProductImageServiceImpl implements PmsProductImageService {

    @Autowired
    PmsProductImageMapper pmsProductImageMapper;
    @Override
    public List<PmsProductImage> getspuImageListBySpuId(String spuId) {
        PmsProductImage pmsProductImage = new PmsProductImage();
        pmsProductImage.setProductId(spuId);
        List<PmsProductImage> select = pmsProductImageMapper.select(pmsProductImage);

        return select;
    }
}
