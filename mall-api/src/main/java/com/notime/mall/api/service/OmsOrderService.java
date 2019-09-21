package com.notime.mall.api.service;

import com.notime.mall.api.bean.OmsOrder;

public interface OmsOrderService {
    void addOmsorder(OmsOrder omsOrder);

    boolean checkTradeCode(String memberId, String tradeCod);

    String generateTradeCode(String memberId);

    void updateOrder(OmsOrder omsOrder);

    void sendWare(OmsOrder omsOrder);
}
