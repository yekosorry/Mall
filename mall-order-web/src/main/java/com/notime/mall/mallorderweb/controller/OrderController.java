package com.notime.mall.mallorderweb.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.notime.mall.annotations.LoginRequired;
import com.notime.mall.api.bean.OmsCartItem;
import com.notime.mall.api.bean.OmsOrder;
import com.notime.mall.api.bean.OmsOrderItem;
import com.notime.mall.api.bean.UmsMemberReceiveAddress;
import com.notime.mall.api.service.OmsCartItemService;
import com.notime.mall.api.service.OmsOrderItemService;
import com.notime.mall.api.service.OmsOrderService;
import com.notime.mall.api.service.UmsMemberReceiveAddressService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class OrderController {
    //点击去结算 跳转订单页面
    //需要拦截

    @Reference
    OmsCartItemService omsCartItemService;

    @Reference
    UmsMemberReceiveAddressService umsMemberReceiveAddressService;

    @RequestMapping("toTrade")
    @LoginRequired
    public  String  trade(HttpServletRequest request, Model model){
        String memberId = (String) request.getAttribute("memberId");
        System.err.println("memberId"+memberId);
        //显示订单信息
        // 前端需要的是 totalAmount  orderDetail  userAddressList
        List<UmsMemberReceiveAddress> userAddressList = null;
        List<OmsOrderItem> orderDetail = null;

        // 遍历购物车 显示购物车被选中的  cartItemList
        // 需要封装成omsorderItem

        List<OmsCartItem> cartItemList = omsCartItemService.getCartListCookie(memberId);
        BigDecimal totalAmount = getTotalAmount(cartItemList);
        orderDetail = new ArrayList<>();
        for (OmsCartItem cartItem : cartItemList) {
           if ("1".equals(cartItem.getIsChecked())){
               OmsOrderItem orderItem = new OmsOrderItem();
               BeanUtils.copyProperties(cartItem,orderItem);

               // 数据弱一致性的校验
                //查询数据库的该商品的价格是否与获取到的价格是否一致
               orderItem.setProductPrice(cartItem.getPrice());


               // 校验库存与 价格  库存在库存系统  调用 库存系统

               orderItem.setProductQuantity(cartItem.getQuantity());
               String orderSn ="yeko"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+System.currentTimeMillis();
               orderItem.setOrderSn(orderSn);
               orderDetail.add(orderItem);
           }

        }
        // 用户的收货信息
        List<UmsMemberReceiveAddress> umsMemberReceiveAddressList=  umsMemberReceiveAddressService.getAllByMemberId(memberId);
        //totalamount


        // 防止页面重复提交  结算页面生产 orderSn

        // input name="tradeCode" type="hidden"  th:value="${tradeCode}"  />
        String   tradeCod = omsOrderService.generateTradeCode(memberId);


        model.addAttribute("totalAmount",totalAmount);
        model.addAttribute("tradeCode",tradeCod);

        model.addAttribute("orderDetailList",orderDetail);

        model.addAttribute("userAddressList",umsMemberReceiveAddressList);

        return "trade";
    }

    private BigDecimal getTotalAmount(List<OmsCartItem> cartItemList) {
        BigDecimal amount = new BigDecimal("0");
        if (cartItemList!=null&&cartItemList.size()>0){
            for (OmsCartItem cartItem : cartItemList) {
               // if (cartItem.getIsChecked()=="1"){ 说一百遍string 不是这样判断的 这样一辈子都是Null
                if(cartItem.getIsChecked().equals("1")){
                    amount = cartItem.getQuantity().multiply(cartItem.getPrice()).add(amount);

                }
            }
            return  amount;
        }
        return  null;

    }




    /*
     * 提交订单
     *       跳转支付页面
     *       生成订单信息
     *       购物车清除
     *
     * */
    @Reference
    OmsOrderService omsOrderService;

    @Reference
    OmsOrderItemService omsOrderItemService;


    @RequestMapping("submitOrder")
    @LoginRequired
    public  String  submitOrder(HttpServletRequest request ,String  addressId , String tradeCode){
        // 获取购物车信息
                //  保存数据库 OmsOrder
                          //     @Transient   List<OmsOrderItem> omsOrderItems;
        String memberId = (String) request.getAttribute("memberId");
        List<OmsCartItem> cartItemList = omsCartItemService.getCartListCookie(memberId);
        OmsOrder omsOrder = new OmsOrder();
        // 封装oreder

        String orderSn = "yeko"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+System.currentTimeMillis();
        omsOrder.setAutoConfirmDay(3);
        omsOrder.setCommentTime(new Date());
        omsOrder.setCreateTime(new Date());
        omsOrder.setMemberUsername((String) request.getAttribute("nickname"));
        omsOrder.setMemberId(memberId);
        omsOrder.setStatus("0");
        omsOrder.setOrderSn(orderSn);
            //收货信息由页面获取
            UmsMemberReceiveAddress umsMemberReceiveAddress    = umsMemberReceiveAddressService.getAllById(addressId);
        omsOrder.setReceiverCity(umsMemberReceiveAddress.getCity());
        omsOrder.setReceiverDetailAddress(umsMemberReceiveAddress.getDetailAddress());
        omsOrder.setReceiverName(umsMemberReceiveAddress.getName());
        omsOrder.setReceiverPhone(umsMemberReceiveAddress.getPhoneNumber());
        omsOrder.setReceiverPostCode(umsMemberReceiveAddress.getPostCode());
        omsOrder.setReceiverRegion(umsMemberReceiveAddress.getRegion());
        omsOrder.setReceiverProvince(umsMemberReceiveAddress.getProvince());

        List<OmsOrderItem> omsOrderItems = new ArrayList<>();

        for (OmsCartItem cartItem : cartItemList) {
            OmsOrderItem orderItem = new OmsOrderItem();
           // 只要勾选的
            if (cartItem.getIsChecked().equals("1")){

                BeanUtils.copyProperties(cartItem,orderItem);
                omsOrderItems.add(orderItem);
            }
        }

        omsOrder.setOmsOrderItems(omsOrderItems);
        omsOrder.setTotalAmount(getTotalAmount(cartItemList));

     //  存入数据库
        omsOrderService.addOmsorder(omsOrder);
        omsOrderItemService.addOrderItemList(omsOrderItems,omsOrder.getId());
        // 购物车清除  db redis
        omsCartItemService.deleteList(cartItemList);

        // 防止页面重复提交  校验 tradeCode
        boolean b = omsOrderService.checkTradeCode(memberId, tradeCode);
        if (b==false){
            return "tradeFail";

        }
        return  "redirect:http://yeko.mall.com:8087/index?orderSn="+orderSn+"&totalAmount="+getTotalAmount(cartItemList);
    }




}
