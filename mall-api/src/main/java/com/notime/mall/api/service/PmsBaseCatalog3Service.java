package com.notime.mall.api.service;

import com.notime.mall.api.bean.PmsBaseCatalog3;

import java.util.List;

public interface PmsBaseCatalog3Service {
    List<PmsBaseCatalog3> getCatalog3ByCatalog2Id(String catalog2Id);
}
