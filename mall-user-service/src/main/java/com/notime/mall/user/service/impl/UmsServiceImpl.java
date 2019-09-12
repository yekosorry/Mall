/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: UmsServiceImpl
 * Author:   Administrator
 * Date:     2019/8/27 21:14
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.notime.mall.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.notime.mall.api.bean.UmsMember;
import com.notime.mall.api.service.UmsMeMberService;
import com.notime.mall.user.mapper.UmsMeMberMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
@Service
public class UmsServiceImpl implements UmsMeMberService {

    @Autowired
    UmsMeMberMapper umsMeMberMapper;
    @Override
    public List<UmsMember> getAll() {
     //   List<UmsMember> umsMemberList = umsMeMberMapper.selectAll();
       // List<UmsMember> umsMemberList = umsMeMberMapper.selectAllUser();
        UmsMember umsMember = new UmsMember();
        umsMember.setId("3");
        System.err.println(umsMember);

        UmsMember umsMember1 = umsMeMberMapper.selectOne(umsMember);
        System.err.println(umsMember1);
        List<UmsMember> list = new ArrayList<>();
        list.add(umsMember1);
        return list;
    }

    @Override
    public UmsMember getUser(String memberId) {

        UmsMember umsMember = new UmsMember();
        umsMember.setId(memberId);
        System.err.println(umsMember);

        UmsMember umsMember1 = umsMeMberMapper.selectOne(umsMember);
        return umsMember1;
    }

    @Override
    public UmsMember loginByUms(UmsMember umsMember) {

        return  umsMeMberMapper.selectOne(umsMember);
    }

    @Override
    public void addUser(UmsMember umsMember) {

          umsMeMberMapper.insertSelective(umsMember);

    }
}
