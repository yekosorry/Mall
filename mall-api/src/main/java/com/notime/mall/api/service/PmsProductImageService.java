package com.notime.mall.api.service;

import com.notime.mall.api.bean.PmsProductImage;

import java.util.List;

public interface PmsProductImageService {

    List<PmsProductImage> getspuImageListBySpuId(String spuId);
}
