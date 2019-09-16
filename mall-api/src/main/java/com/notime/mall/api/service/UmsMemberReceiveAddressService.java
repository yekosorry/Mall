package com.notime.mall.api.service;

import com.notime.mall.api.bean.UmsMemberReceiveAddress;

import java.util.List;

public interface UmsMemberReceiveAddressService {
    List<UmsMemberReceiveAddress> getAllByMemberId(String memberId);

    UmsMemberReceiveAddress getAllById(String addrssId);
}
