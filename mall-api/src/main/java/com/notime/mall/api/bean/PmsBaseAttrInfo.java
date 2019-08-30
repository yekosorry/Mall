package com.notime.mall.api.bean;


import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @param
 * @return
 */
public class PmsBaseAttrInfo implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column
    private String id;
    @Column
    private String attrName;
    @Column
    private String catalog3Id;
    @Column
    private String isEnabled;
    @Transient
    List<PmsBaseAttrValue> attrValueList;


    // 页面传来的 attrId  没有这个属性添加一个
    // 没有set get 给它
    // 对应的是id
    @Transient
    private String attrId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        this.attrId =id;
        //  给id 值的时候 把attrId也赋值了
        //  通过attrId 就能找到了
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public String getCatalog3Id() {
        return catalog3Id;
    }

    public void setCatalog3Id(String catalog3Id) {
        this.catalog3Id = catalog3Id;

    }

    public String getAttrId() {
        return attrId;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }

    public String getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(String isEnabled) {
        this.isEnabled = isEnabled;
    }

    public List<PmsBaseAttrValue> getAttrValueList() {
        return attrValueList;
    }

    public void setAttrValueList(List<PmsBaseAttrValue> attrValueList) {
        this.attrValueList = attrValueList;
    }

    @Override
    public String toString() {
        return "PmsBaseAttrInfo{" +
                "id='" + id + '\'' +
                ", attrName='" + attrName + '\'' +
                ", catalog3Id='" + catalog3Id + '\'' +
                ", isEnabled='" + isEnabled + '\'' +
                ", attrValueList=" + attrValueList +
                ", attrId='" + attrId + '\'' +
                '}';
    }
}
