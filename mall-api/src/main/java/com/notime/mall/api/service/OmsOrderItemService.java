package com.notime.mall.api.service;

import com.notime.mall.api.bean.OmsOrderItem;

import java.util.List;

public interface OmsOrderItemService {
    void addOrderItemList(List<OmsOrderItem> omsOrderItems, String id);
}
