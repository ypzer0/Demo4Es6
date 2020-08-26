package com.es6.demo.entity;


import com.es6.demo.annotation.InnerHits;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Author: yangpeng
 * @ClassName: EnterpriseIndex
 * @Description: todo
 * @Date: 2020/8/24 19:54
 * @Version v1.0
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
    @Field(type = FieldType.Text,analyzer="ik_smart", searchAnalyzer="ik_smart", store = true)
    private String name;
    /**
     * 企业品牌
     */
    @Field(type = FieldType.Text,analyzer="ik_smart", searchAnalyzer="ik_smart", store = true)
    private String brand;
    /**
     * 企业介绍
     */
    @Field(type = FieldType.Text,analyzer="ik_smart", searchAnalyzer="ik_smart", store = true)
    private String introduction;

    @Field(type = FieldType.Date, store = true)
    private Date createTime;
    /**
     * 企业下产品的所有分类
     */
    @Field(type = FieldType.Keyword,store = true)
    private Set<String> categoryIds;

    @Field(type = FieldType.Nested)
    private List<ProductInEnterprise> products;

    @InnerHits(name = "com.es6.demo.entity.ProductInEnterprise",fieldName = "products")
    private List<ProductInEnterprise> innerHits;

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

    public Set<String> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(Set<String> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public List<ProductInEnterprise> getProducts() {
        return products;
    }

    public void setProducts(List<ProductInEnterprise> products) {
        this.products = products;
    }

    public List<ProductInEnterprise> getInnerHits() {
        return innerHits;
    }

    public void setInnerHits(List<ProductInEnterprise> innerHits) {
        this.innerHits = innerHits;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}
