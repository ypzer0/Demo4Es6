package com.es6.demo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.List;

/**
 *@Description: 产品索引表
 *@Author: Lin Lee
 *@Date: 2019/5/10 15:28
 */
@Document(indexName = "product",type = "_doc")
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
    @Field(type = FieldType.Text,analyzer="ik_smart", searchAnalyzer="ik_smart", store = true)
    @ScriptedField(name = "name")
    private String name;
    /**价格*/
    @Field(type= FieldType.Double,index =true, store = true)
    private Double price;

    @Field(type= FieldType.Nested, store = true)
    private List<CategoryInProduct> categories;

    @Override
    public String toString() {
        return "ProductIndex{" +
                "id='" + id + '\'' +
                ", enterpriseId='" + enterpriseId + '\'' +
                ", enterpriseInfo=" + enterpriseInfo +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", categories=" + categories +
                '}';
    }

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



    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<CategoryInProduct> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryInProduct> categories) {
        this.categories = categories;
    }
}
