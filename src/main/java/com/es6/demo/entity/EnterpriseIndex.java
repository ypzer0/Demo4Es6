package com.es6.demo.entity;


import com.es6.demo.annotation.InnerHits;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *@Description: 企业（工厂）索引表
 *@Author: Lin Lee
 *@Date: 2019/5/10 15:28
 */
@Document(indexName = "enterprise", type = "_doc")
@Setting(settingPath = "enterprise/enterprise-setting.json")
public class EnterpriseIndex implements Serializable {

    /***
     * PS
     * FieldType.Keyword：不做分词的string类型
     * FieldType.Text：做分词的string类型
     */
    @Id
    @Field(type= FieldType.Keyword, store = true)
    private String id;
    /**公司名称*/
    @Field(type = FieldType.Text,analyzer="ik_pinyin_analyzer", searchAnalyzer="ik_pinyin_analyzer", store = true)
    private String name;
    /**
     * 企业品牌
     */
    @Field(type = FieldType.Text,analyzer="ik_pinyin_analyzer", searchAnalyzer="ik_pinyin_analyzer", store = true)
    private String brand;
    /**
     * 企业介绍
     */
    @Field(type = FieldType.Text,analyzer="ik_pinyin_analyzer", searchAnalyzer="ik_pinyin_analyzer", store = true)
    private String introduction;

    @Field(type = FieldType.Date, store = true)
    private Date createTime;

    @Field(type = FieldType.Nested)
    private List<ProductToEnterpriseIndex> products;

    @InnerHits(name = "com.es6.demo.entity.ProductToEnterpriseIndex",fieldName = "products")
    private List<ProductToEnterpriseIndex> innerHits;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<ProductToEnterpriseIndex> getProducts() {
        return products;
    }

    public void setProducts(List<ProductToEnterpriseIndex> products) {
        this.products = products;
    }

    public List<ProductToEnterpriseIndex> getInnerHits() {
        return innerHits;
    }

    public void setInnerHits(List<ProductToEnterpriseIndex> innerHits) {
        this.innerHits = innerHits;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}
