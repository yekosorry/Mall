package com.notime.mall.listener;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.notime.mall.api.bean.PaymentInfo;
import com.notime.mall.api.service.PaymentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
public class PayConsumer {


    @Autowired
    PaymentInfoService paymentInfoService;

    @JmsListener(destination = "PAYMENT_CHECK", containerFactory = "jmsListener")
    public void checkPay(MapMessage mapMessage) throws JMSException {

        String orderSn = mapMessage.getString("orderSn");
        int count = mapMessage.getInt("count");

        // 查询是否支付
        //支付状态   paymentInfo 中有个状态
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setOrderSn(orderSn);  // 后面继续查询 需要ordersn
        // 将返回的支付状态装进 PaymentInfo
        String paymentStatus = checkPayStatus(orderSn);
        /*交易状态：WAIT_BUYER_PAY（交易创建，等待买家付款）  继续查询  null
        、TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）  修改支付信息
        、TRADE_SUCCESS（交易支付成功）    修改支付信息
        、TRADE_FINISHED（交易结束，不可退款）   修改支付信息
        * */

        // 找到要修改的payinfo
        PaymentInfo paymentInfoForUpdate =   paymentInfoService.getInfoByOrderSn(orderSn);

      //  if (paymentStatus==null || paymentStatus =="WAIT_BUYER_PAY" ){
        if (paymentStatus==null || "WAIT_BUYER_PAY".equals(paymentStatus) ){
            if (count>0){
                count--;
                // 继续消息
                paymentInfoService.sendPaymentCheck( paymentInfo,  count);
                System.out.println("执行支付检查任务，检查失败，继续发送检查队列，剩余次数" + count + "次");
            }else{
                System.out.println("剩余任务次数为" + count + "，不再继续检查");

            }
        }else {
            //修改订单信息
            System.out.println("已经支付成功，更新支付信息，发送支付成功队列。。。");
            PaymentInfo info = new PaymentInfo();
            info.setPaymentStatus("已支付");
            info.setCallbackTime(new Date());
            info.setCallbackContent("检查成功");
            info.setOrderSn(orderSn);
            // 修改支付系统
            paymentInfoService.updatePay(info);
            // 修改订单系统   有个消息可以用
            paymentInfoService.sendPaymentQueue(info);

        // out_trade_no与trade_no的区别是什么
            // outTradeNo 商家订单号  TradeNo 支付宝交易订单号
        }

    }

    @Autowired
    AlipayClient alipayClient;


    private String checkPayStatus(String orderSn) {
        // 查询开发者文档
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        /*out_trade_no	String	特殊可选	64
        订单支付时传入的商户订单号,
        和支付宝交易号不能同时为空。
         trade_no,out_trade_no如果同时存在优先取trade_no
        20150320010101001
        * */
        // 将请求参数装进map
        Map<String ,Object> map =new HashMap<>();
        map.put("out_trade_no",orderSn);


        request.setBizContent(JSON.toJSONString(map));
        AlipayTradeQueryResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }



        if (response.isSuccess()) {
            System.err.println(response.getTradeStatus());
            System.out.println("调用成功");
        } else {
            System.err.println(response.getTradeStatus());
            System.out.println("调用失败");
        }

        return  response.getTradeStatus();
    }

}
