package com.notime.mall.order.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.notime.mall.api.bean.OmsOrderItem;
import com.notime.mall.api.service.OmsOrderItemService;
import com.notime.mall.order.mapper.OmsOrderItemMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class OmsOrderItemServiceImpl implements OmsOrderItemService  {
    @Autowired
    OmsOrderItemMapper omsOrderItemMapper;

    @Override
    public void addOrderItemList(List<OmsOrderItem> omsOrderItems, String id) {
        for (OmsOrderItem omsOrderItem : omsOrderItems) {
                omsOrderItem.setOrderId(id);
            omsOrderItemMapper.insertSelective(omsOrderItem);
        }
    }
}
