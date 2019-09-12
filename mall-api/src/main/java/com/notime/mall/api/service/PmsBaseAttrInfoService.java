package com.notime.mall.api.service;

import com.notime.mall.api.bean.PmsBaseAttrInfo;

import java.util.List;

public interface PmsBaseAttrInfoService {

    List<PmsBaseAttrInfo> getAttrInfoList(String catalog3Id);

    void saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo);

    PmsBaseAttrInfo getAttrValueList(String attrId);

    List<PmsBaseAttrInfo> getLIstAttrInfoByValueId(String join);
}
