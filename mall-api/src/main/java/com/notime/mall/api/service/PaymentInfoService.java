package com.notime.mall.api.service;

import com.notime.mall.api.bean.PaymentInfo;

public interface PaymentInfoService {
    void addPaymentInfo(PaymentInfo paymentInfo);

    void updatePay(PaymentInfo paymentInfo);


    void sendPaymentCheck(PaymentInfo paymentInfo, int count);

    void sendPaymentQueue(PaymentInfo paymentInfo);

    PaymentInfo getInfoByOrderSn(String orderSn);
}
