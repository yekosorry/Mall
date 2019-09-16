package com.notime.mall.contrller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;

@Controller
public class PayController {

    @RequestMapping("index")
    public  String  index(Model model , BigDecimal totalAmount, String orderSn){
        model.addAttribute("totalAmount",totalAmount);
        model.addAttribute("orderSn",orderSn);
        return  "index.html";
    }
}
