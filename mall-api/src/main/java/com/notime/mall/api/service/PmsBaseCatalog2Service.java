package com.notime.mall.api.service;

import com.notime.mall.api.bean.PmsBaseCatalog2;

import java.util.List;

public interface PmsBaseCatalog2Service {
    List<PmsBaseCatalog2> getCatalog2ByCatalogId(String catalog1Id);
}
