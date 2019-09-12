package com.notime.mall.api.service;

import com.notime.mall.api.bean.OmsCartItem;

import java.util.List;

public interface OmsCartItemService {
    void addCartItem(OmsCartItem omsCartItem);

    OmsCartItem isExitsCart(OmsCartItem omsCartItem);

    void updateCartItem(OmsCartItem omsCartItemForDb);

    List<OmsCartItem> getCartListCookie(String memberId);

    OmsCartItem checkCart( String productSkuId, String memberId,String isChecked);
}
