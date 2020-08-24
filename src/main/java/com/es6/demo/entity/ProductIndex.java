package com.es6.demo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.Date;
import java.util.List;

/**
 *@Description: 产品索引表
 *@Author: Lin Lee
 *@Date: 2019/5/10 15:28
 */
@Document(indexName = "product",type = "_doc")
@Setting(settingPath = "product/product-setting.json")
public class ProductIndex {
    /***
     * PS
     * FieldType.Keyword：不做分词的string类型
     * FieldType.Text：做分词的string类型
     */
    @Id
    @Field(type= FieldType.Keyword,index =false, store = true)
    private String id;
    @Field(type= FieldType.Keyword,store = true)
    private String enterpriseId;
    @Field(type= FieldType.Object,store = true)
    private EnterpriseIndex enterpriseInfo;
    /**产品名称*/
    @Field(type = FieldType.Text,analyzer="ik_pinyin_analyzer", searchAnalyzer="ik_pinyin_analyzer", store = true)
    // @ScriptedField(name = "name")
    private String name;
    /**
     * 产品参数
     */
    @Field(type = FieldType.Text,analyzer="ik_pinyin_analyzer", searchAnalyzer="ik_pinyin_analyzer", store = true)
    private String parameter;
    /**
     * 产品所属企业名称
     */
    @Field(type = FieldType.Text,analyzer="ik_pinyin_analyzer", searchAnalyzer="ik_pinyin_analyzer", store = true)
    private String enterpriseName;
    /**
     * 简介
     */
    @Field(type = FieldType.Text,analyzer="ik_pinyin_analyzer", searchAnalyzer="ik_pinyin_analyzer", store = true)
    private String introduction;

    @Field(type = FieldType.Date, store = true)
    private Date createTime;

    @Field(type= FieldType.Nested, store = true)
    private List<CategoryInProduct> categories;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public EnterpriseIndex getEnterpriseInfo() {
        return enterpriseInfo;
    }

    public void setEnterpriseInfo(EnterpriseIndex enterpriseInfo) {
        this.enterpriseInfo = enterpriseInfo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<CategoryInProduct> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryInProduct> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "ProductIndex{" +
                "id='" + id + '\'' +
                ", enterpriseId='" + enterpriseId + '\'' +
                ", enterpriseInfo=" + enterpriseInfo +
                ", name='" + name + '\'' +
                ", parameter='" + parameter + '\'' +
                ", enterpriseName='" + enterpriseName + '\'' +
                ", introduction='" + introduction + '\'' +
                ", createTime=" + createTime +
                ", categories=" + categories +
                '}';
    }
}
