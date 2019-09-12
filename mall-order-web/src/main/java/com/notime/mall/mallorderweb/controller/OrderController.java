package com.notime.mall.mallorderweb.controller;


import com.notime.mall.annotations.LoginRequired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class OrderController {
    //点击去结算 跳转订单页面
    //需要拦截

    @RequestMapping("toTrade")
    @LoginRequired
    public  String  trade(HttpServletRequest request){
        String memberId = (String) request.getAttribute("memberId");
        System.err.println("memberId"+memberId);

        return "trade";
    }
}
