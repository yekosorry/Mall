/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: umsMember
 * Author:   Administrator
 * Date:     2019/8/27 11:41
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.notime.mall.api.bean;

import java.io.Serializable;

public class UmsMember  implements Serializable {

    private String id;
    private String memberLevelId;
    private String username;
    private String password;
    private String nickname;
    private String phone;
    private String status;
    private String createTime;
    private String icon;
    private String gender;
    private String birthday;
    private String city;
    private String job;
    private String personalizedSignature;
    private String sourceUid;
    private String sourceType;
    private String integration;
    private String growth;
    private String luckeyCount;
    private String accessToken;
    private String accessCode;
    private String historyIntegration;


    public UmsMember() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberLevelId() {
        return memberLevelId;
    }

    public void setMemberLevelId(String memberLevelId) {
        this.memberLevelId = memberLevelId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getPersonalizedSignature() {
        return personalizedSignature;
    }

    public void setPersonalizedSignature(String personalizedSignature) {
        this.personalizedSignature = personalizedSignature;
    }

    public String getSourceUid() {
        return sourceUid;
    }

    public void setSourceUid(String sourceUid) {
        this.sourceUid = sourceUid;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getIntegration() {
        return integration;
    }

    public void setIntegration(String integration) {
        this.integration = integration;
    }

    public String getGrowth() {
        return growth;
    }

    public void setGrowth(String growth) {
        this.growth = growth;
    }

    public String getLuckeyCount() {
        return luckeyCount;
    }

    public void setLuckeyCount(String luckeyCount) {
        this.luckeyCount = luckeyCount;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public String getHistoryIntegration() {
        return historyIntegration;
    }

    public void setHistoryIntegration(String historyIntegration) {
        this.historyIntegration = historyIntegration;
    }

    public UmsMember(String id, String memberLevelId, String username, String password, String nickname, String phone, String status, String createTime, String icon, String gender, String birthday, String city, String job, String personalizedSignature, String sourceUid, String sourceType, String integration, String growth, String luckeyCount, String accessToken, String accessCode, String historyIntegration) {
        this.id = id;
        this.memberLevelId = memberLevelId;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.phone = phone;
        this.status = status;
        this.createTime = createTime;
        this.icon = icon;
        this.gender = gender;
        this.birthday = birthday;
        this.city = city;
        this.job = job;
        this.personalizedSignature = personalizedSignature;
        this.sourceUid = sourceUid;
        this.sourceType = sourceType;
        this.integration = integration;
        this.growth = growth;
        this.luckeyCount = luckeyCount;
        this.accessToken = accessToken;
        this.accessCode = accessCode;
        this.historyIntegration = historyIntegration;
    }
}
