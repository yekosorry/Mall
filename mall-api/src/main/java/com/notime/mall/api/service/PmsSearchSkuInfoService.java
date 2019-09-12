package com.notime.mall.api.service;

import com.notime.mall.api.bean.PmsSearchParam;
import com.notime.mall.api.bean.PmsSearchSkuInfo;

import java.util.List;

public interface PmsSearchSkuInfoService {
    List<PmsSearchSkuInfo> search(PmsSearchParam pmsSearchParam);
}

