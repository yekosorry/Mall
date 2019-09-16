package com.notime.mall.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.notime.mall.api.bean.UmsMemberReceiveAddress;
import com.notime.mall.api.service.UmsMemberReceiveAddressService;
import com.notime.mall.user.mapper.UmsMemberReceiveAddressMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class UmsMemberReceiveAddressServiceImpl  implements UmsMemberReceiveAddressService {
    @Autowired
    UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;

    @Override
    public List<UmsMemberReceiveAddress> getAllByMemberId(String memberId) {
        UmsMemberReceiveAddress umsMemberReceiveAddress = new UmsMemberReceiveAddress();
        umsMemberReceiveAddress.setMemberId(memberId);

        return umsMemberReceiveAddressMapper.select(umsMemberReceiveAddress);
    }

    @Override
    public UmsMemberReceiveAddress getAllById(String addrssId) {

        UmsMemberReceiveAddress umsMemberReceiveAddress = new UmsMemberReceiveAddress();
        umsMemberReceiveAddress.setId(addrssId);


        UmsMemberReceiveAddress umsMemberReceiveAddress1 = umsMemberReceiveAddressMapper.selectOne(umsMemberReceiveAddress);
        return umsMemberReceiveAddress1;
    }
}
