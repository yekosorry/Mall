package com.notime.mall.api.service;


import com.notime.mall.api.bean.UmsMember;

import java.util.List;

public interface UmsMeMberService {
    List<UmsMember> getAll();

    UmsMember getUser(String memberId);

    UmsMember loginByUms(UmsMember umsMember);

    void addUser(UmsMember umsMember);
}
