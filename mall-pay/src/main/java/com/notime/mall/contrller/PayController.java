package com.notime.mall.contrller;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.notime.mall.annotations.LoginRequired;
import com.notime.mall.api.bean.PaymentInfo;
import com.notime.mall.api.service.PaymentInfoService;
import com.notime.mall.config.AlipayConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 */
@Controller
public class PayController {

    @RequestMapping("index")
    public  String  index(Model model , BigDecimal totalAmount, String orderSn){
        model.addAttribute("totalAmount",totalAmount);
        model.addAttribute("orderSn",orderSn);
        return  "index.html";
    }
    //   $("#paymentForm").attr("action","/"+$("input[type='radio']:checked").val()+"/submit")  ;


    @Autowired
    AlipayClient alipayClient;

   @Autowired
    PaymentInfoService paymentInfoService;


    @RequestMapping("alipay/submit")
    @ResponseBody // 返回form 提交form
    @LoginRequired  // 获取memberId  从数据库中查询
    public  String  alipay(HttpServletRequest request, BigDecimal totalAmount, String orderSn){
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
        alipayRequest.setReturnUrl(AlipayConfig.return_url);
        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);//在公共参数中设置回跳和通知地址
        String memberId = (String) request.getAttribute("memberId");
        Map<String, Object> map = new HashMap<>();
        map.put("out_trade_no",orderSn);
        map.put("product_code","FAST_INSTANT_TRADE_PAY");
        map.put("total_amount",0.01);
        map.put("subject","999感冒灵");

        alipayRequest.setBizContent(JSON.toJSONString(map));//填充业务参数
        String form="";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }


        // 生成支付信息PaymentInfo
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setOrderSn(orderSn);
        paymentInfo.setCreateTime(new Date());
        paymentInfo.setPaymentStatus("未支付");
        paymentInfo.setTotalAmount(totalAmount);
        paymentInfo.setSubject("Someone You Loved");


        paymentInfoService.addPaymentInfo(paymentInfo);


        //  检查是否支付成功
        paymentInfoService.sendPaymentCheck(paymentInfo,7);


        return  form;
    }


    // public static String notify_url = "http://yeko.mall.com:8087/alipay/callback/return";
        //  异步通知客户

    @LoginRequired   // 有这个才是同一个request  才能获取到ordersn
    @RequestMapping("alipay/callback/return")
    public String  callBackReturn(HttpServletRequest request){
        // 修改支付信息
            //生成订单阿

        //找到要更新的Pay
        /*
        * http://yeko.mall.com:8087/alipay/callback/return?charset=utf-8
        * &out_trade_no=yeko201909161837391568630259105
        * &method=alipay.trade.page.pay.return&
        * total_amount=0.01&sign=A9fL8%2B8Q0XjCs2mVS6RJU8Qf5PN5%2F9btBK5CgAbxoTPEs41aZawHaoEKgOeKAPRSF0Z4ztz28s65GpRAG%2BbXUR%2BAUSDP3r3y2HxevtSMP09jdqygWA9r0qd1gOu38N4pzIeDvSnTsPvBvnICt%2F%2BFPccekhQbhnMuYXvtwfB0u7eqYH6kxvQUlxgumXGP6LJwTcCAD23ccczhQqr4EWUEqYAnxaeyRxVTD1vxHzbp9cVN8p246o5lR85lZsZte4dvfgKiJ2NYqG5XwyP3yp8ASV1Lbdr123kg6NhWolxyoJc8rgzMdKdhW%2B5HCV2iQxZlDuKgW1VgjuNXj7QEtvjNbA%3D%3D&trade_no=2019091622001474320551811786&auth_app_id=2018020102122556&version=1.0
        * &app_id=2018020102122556&sign_type=RSA2&seller_id=2088921750292524&timestamp=2019-09-16+18%3A38%3A10
        * */

        PaymentInfo paymentInfo = new PaymentInfo();
    // paymentInfo.setOrderSn(request.getParameter("outTradeNo"));
    paymentInfo.setOrderSn(request.getParameter("out_trade_no"));
        paymentInfo.setCallbackContent(request.getQueryString());
        paymentInfo.setCallbackTime(new Date());
        paymentInfo.setPaymentStatus("已支付");
        //更新订单状态
        paymentInfoService.updatePay(paymentInfo);

        // 消息队列

        // 更新支付信息
        paymentInfoService.sendPaymentQueue(paymentInfo);




        //库存
        //物流

        return "finish";
    }
}

