package com.notime.mall.user.mapper;

import com.notime.mall.api.bean.UmsMember;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
public interface UmsMeMberMapper  extends Mapper<UmsMember> {
    // tk.mapper 在api 中导入
    List<UmsMember> selectAllUser();

}
