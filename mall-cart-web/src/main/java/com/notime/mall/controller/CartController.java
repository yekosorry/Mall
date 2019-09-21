package com.notime.mall.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.notime.mall.annotations.LoginRequired;
import com.notime.mall.util.CookieUtil;
import com.notime.mall.util.MyCookieUtil;
import com.notime.mall.api.bean.OmsCartItem;
import com.notime.mall.api.bean.PmsSkuInfo;
import com.notime.mall.api.service.OmsCartItemService;
import com.notime.mall.api.service.PmsSkuInfoService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class CartController {
    //点击加入购物车跳转页面  addToCart

    @Reference
    PmsSkuInfoService pmsSkuInfoService;

    @Reference
    OmsCartItemService omsCartItemService;

    @LoginRequired(needSuccess = false)
    @RequestMapping("addToCart")
    public String addToCart(Model model,OmsCartItem omsCartItem, HttpServletRequest request, HttpServletResponse response) {

        // 返回给购物车的是 OmsCartItemList
        List<OmsCartItem> omsCartItemList = new ArrayList<>();

        String memberId = (String) request.getAttribute("memberId");
        System.err.println(memberId);
        PmsSkuInfo skuinfo = pmsSkuInfoService.getSkuInfoById(omsCartItem.getProductSkuId());
        BigDecimal quantity = omsCartItem.getQuantity();
        //  StringUtils
        //   判断是否登录  单点登录功能未实现 字符串模拟未登录
        if (StringUtils.isBlank(memberId)) {
            // cookie  有没有以前的数据
           String cartListCookie = MyCookieUtil.getCookieValue(request, "cartListCookie", true);

            if (StringUtils.isBlank(cartListCookie)) {
                // 没有数据  直接加入就行了
                OmsCartItem cartItem = new OmsCartItem();
                //  根据skuId 查商品信息



                    cartItem.setIsChecked("1");  // 被选中
                    cartItem.setProductSkuId(skuinfo.getId());  // productId  与productskuId 的区别是什么  一个是商品id 一个是skuid?
                    cartItem.setPrice(skuinfo.getPrice());
                    cartItem.setQuantity(quantity);
                    cartItem.setProductPic(skuinfo.getSkuDefaultImg());
                    cartItem.setProductCategoryId(skuinfo.getCatalog3Id());
                    cartItem.setProductId(skuinfo.getProductId());
                    cartItem.setCreateDate(new Date());
                    cartItem.setTotalPrice(quantity.multiply(cartItem.getPrice()));
                    cartItem.setProductName(skuinfo.getSkuName());


                    // 保存为cartlistcookie
                    omsCartItemList.add(cartItem);
            } else {
                // 重复的数量添加  没有的就新增
                //  看看cookie的数据
                List<OmsCartItem> cartListForCache = JSON.parseArray(cartListCookie, OmsCartItem.class);
               // 跟页面的进行比较
                boolean flag = ifNewCart(cartListForCache,omsCartItem);
                if(flag){
                    // 新的
                    omsCartItem.setIsChecked("1");  // 被选中
                    omsCartItem.setProductSkuId(skuinfo.getId());  // productId  与productskuId 的区别是什么  一个是商品id 一个是skuid?
                    omsCartItem.setPrice(skuinfo.getPrice());
                    omsCartItem.setQuantity(quantity);
                    omsCartItem.setProductPic(skuinfo.getSkuDefaultImg());
                    omsCartItem.setProductCategoryId(skuinfo.getCatalog3Id());
                    omsCartItem.setProductId(skuinfo.getProductId());
                    omsCartItem.setCreateDate(new Date());
                    omsCartItem.setTotalPrice(quantity.multiply(omsCartItem.getPrice()));
                    omsCartItem.setProductName(skuinfo.getSkuName());

                    //  cartListForCache.add(omsCartItem);
                    // 以前有的也要加进来
                    for (OmsCartItem cartItem : cartListForCache) {
                        omsCartItemList.add(cartItem);
                    }
                    omsCartItemList.add(omsCartItem);

                }else {
                    omsCartItem.setIsChecked("1");  // 被选中
                    omsCartItem.setProductSkuId(skuinfo.getId());  // productId  与productskuId 的区别是什么  一个是商品id 一个是skuid?
                    omsCartItem.setPrice(skuinfo.getPrice());
                    omsCartItem.setQuantity(quantity);
                    omsCartItem.setProductPic(skuinfo.getSkuDefaultImg());
                    omsCartItem.setProductCategoryId(skuinfo.getCatalog3Id());
                    omsCartItem.setProductId(skuinfo.getProductId());
                    omsCartItem.setCreateDate(new Date());
                    omsCartItem.setTotalPrice(quantity.multiply(omsCartItem.getPrice()));
                    omsCartItem.setProductName(skuinfo.getSkuName());
                    // 遍历比较 修改数量和totalprice

                    for (OmsCartItem cartItem : cartListForCache) {
                      //  if (cartItem.getId().equals(omsCartItem.getId())){
                        if (cartItem.getProductSkuId().equals(omsCartItem.getProductSkuId())){
                            cartItem.setQuantity(cartItem.getQuantity().add(omsCartItem.getQuantity()));
                            cartItem.setTotalPrice(cartItem.getPrice().multiply(cartItem.getQuantity()));
                        }
                        omsCartItemList.add(cartItem);
                    }
                    //  修改的是cartListForCache  覆盖的却是omsCartItemList 等于没做

                }
            }
            //  cookie在 浏览器上 需要覆盖
            CookieUtil.setCookie(request, response, "cartListCookie", JSON.toJSONString(omsCartItemList), 1000 * 60 * 30, true);
        }

        else {
            //登录操作db
            // 通过查询用户购物车  购物车的信息有用户的id    与传入的购物信息进行比较
            //  没有直接加
            omsCartItem.setMemberId(memberId);
            omsCartItem.setIsChecked("1");  // 被选中
            omsCartItem.setProductSkuId(skuinfo.getId());  // productId  与productskuId 的区别是什么  一个是商品id 一个是skuid?
            omsCartItem.setPrice(skuinfo.getPrice());
            omsCartItem.setQuantity(quantity);
            omsCartItem.setProductPic(skuinfo.getSkuDefaultImg());
            omsCartItem.setProductCategoryId(skuinfo.getCatalog3Id());
            omsCartItem.setProductId(skuinfo.getProductId());
            omsCartItem.setCreateDate(new Date());
            omsCartItem.setTotalPrice(quantity.multiply(omsCartItem.getPrice()));
            omsCartItem.setProductName(skuinfo.getSkuName());
            //这个omsCartItem 属性太少了 要自己添加
            // 商品是否在用户的购物车里  存在的话拿出来 改价格  和小计
            OmsCartItem omsCartItemForDb =  omsCartItemService.isExitsCart(omsCartItem);
            if (omsCartItemForDb != null){
                // 添加过该商品
                omsCartItemForDb.setQuantity(omsCartItemForDb.getQuantity().add(omsCartItem.getQuantity()));
                omsCartItemForDb.setTotalPrice(omsCartItemForDb.getQuantity().multiply(omsCartItemForDb.getPrice()));
                // 修改完把数据 返回数据库  修改数据库啊
                omsCartItemService.updateCartItem(omsCartItemForDb);
            }else{
                omsCartItemService.addCartItem(omsCartItem);
            }

        }
        //model.addAttribute("skuInfo",skuinfo);
       // model.addAttribute("skuNum",quantity);
      //  return "success.html";
        return "redirect:/success.html";

    }

    private boolean ifNewCart(List<OmsCartItem> cartListForCache, OmsCartItem omsCartItem) {
        boolean flag = true ;
        for (OmsCartItem cartItem : cartListForCache) {
            if (!ObjectUtils.notEqual(cartItem.getProductSkuId(),omsCartItem.getProductSkuId())){
                flag =false;
            }

        }
        return flag;
    }

    // 去购物车
    @LoginRequired(needSuccess = false)
    @RequestMapping("cartList")
    public String  cartList(HttpServletRequest request,Model model){

        System.err.println(request);
        // 前端需要cartList  totalAcount
        //判断是否登录  登录 从redis拿 未登录从cookie中拿
        String memberId = (String) request.getAttribute("memberId");
        System.err.println(memberId);
        List<OmsCartItem> omsCartItemList = null;
        if (StringUtils.isBlank(memberId)){
            String cartListCookie = MyCookieUtil.getCookieValue(request, "cartListCookie", true);
        //   T t = JSON.toJavaObject(cartListCookie, OmsCartItem.class);
            if (StringUtils.isNotBlank(cartListCookie)){
               omsCartItemList = JSON.parseArray(cartListCookie, OmsCartItem.class);
            }
        }else {
            omsCartItemList =   omsCartItemService.getCartListCookie(memberId);
        }

        model.addAttribute("cartList",omsCartItemList);
        model.addAttribute("totalAcount",gettotalAcount(omsCartItemList));

        //return "cartList.html";
        return "cartList";
    }

    private BigDecimal gettotalAcount(List<OmsCartItem> omsCartItemList) {
        BigDecimal account = new BigDecimal("0");
       if (omsCartItemList!=null&&omsCartItemList.size()>0){
      // if (omsCartItemList.size()>0&&omsCartItemList!=null){  // 这样会报空指针啊
           for (OmsCartItem cartItem : omsCartItemList) {
              //if (cartItem.getIsChecked()=="1"){
              if (cartItem.getIsChecked().equals("1")){
         //     account = cartItem.getTotalPrice().add(account);
                 account = cartItem.getPrice().multiply(cartItem.getQuantity()).add(account);
              }
           }
       }
        return  account;
    }
    @LoginRequired(needSuccess = false)
    @RequestMapping("checkCart")
    public String checkCart(HttpServletRequest request, HttpServletResponse response,Model model,String  skuId ,String isChecked){
// var param="isChecked="+isCheckedFlag+"&"+"skuId="+skuId;
        System.err.println(skuId+"productSkuId productSkuId ");
        System.err.println(isChecked+"isChecked isChecked ");
           // 能从前端获取的是什么？productSkuId isChecked
     // 刷新购物车
        // 判断是是否登录
        String memberId = (String) request.getAttribute("memberId");
        System.err.println(memberId);
        //  登录了才能找 找到取消勾选或勾选的商品   修改它的ischecked  更新的时候db redis 都更新
    //  OmsCartItem omsCartItem=  omsCartItemService.checkCart(productSkuId,memberId,isChecked);


        List<OmsCartItem> omsCartItemList = null;

            if (StringUtils.isNotBlank(memberId)){
                OmsCartItem omsCartItem=  omsCartItemService.checkCart(skuId,memberId,isChecked);
                // 刷新数据库
                // 已经更新了 直接获取 返回给前端就行了
                omsCartItemList = omsCartItemService.getCartListCookie(memberId);

            }else {
                //刷新cookie
                String cartListCookie = MyCookieUtil.getCookieValue(request, "cartListCookie", true);
                 omsCartItemList = JSON.parseArray(cartListCookie, OmsCartItem.class);
                //通过skuid memberid 找到该商品  修改它的ischecked
                for (OmsCartItem cartItem : omsCartItemList) {
                    if (cartItem.getMemberId()==memberId&&cartItem.getProductSkuId()==skuId){
                        cartItem.setIsChecked(isChecked);
                    }
                }
                //保存cookie
                CookieUtil.setCookie(request, response, "cartListCookie", JSON.toJSONString(omsCartItemList), 1000 * 60 * 30, true);
            }

        for (OmsCartItem omsCartItem : omsCartItemList) {
            omsCartItem.setTotalPrice(omsCartItem.getPrice().multiply(omsCartItem.getQuantity()));
            System.err.println(omsCartItem.getTotalPrice());
        }
        System.err.println(omsCartItemList);
        model.addAttribute("cartList",omsCartItemList);
        model.addAttribute("totalAcount",gettotalAcount(omsCartItemList));
       // return  "InnercartList.html";
        return  "InnercartList";

    }

}
