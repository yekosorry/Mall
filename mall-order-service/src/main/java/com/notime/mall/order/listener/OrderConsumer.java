package com.notime.mall.order.listener;

import com.notime.mall.api.bean.OmsOrder;
import com.notime.mall.api.service.OmsOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import java.util.Date;

@Component
public class OrderConsumer {
    @Autowired
    OmsOrderService orderService;
  //PAYMENT_SUCCESS
    @JmsListener(containerFactory = "jmsListener",destination ="PAYMENT_SUCCESS" )
    public void consumer(MapMessage mapMessage) throws JMSException {

        // 收到信息 就执行下面的操作
        String orderSn = mapMessage.getString("orderSn");


        // 更新订单信息
        OmsOrder omsOrder = new OmsOrder();
        omsOrder.setOrderSn(orderSn);
        omsOrder.setStatus("1");
        omsOrder.setPayType(1);
        omsOrder.setPaymentTime(new Date());

        orderService.updateOrder(omsOrder);

        // 发送库存信息
        orderService.sendWare(omsOrder);

    }
}
