package com.es6.demo.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.ScriptedField;

import java.io.Serializable;
import java.util.List;

/**
 *@Description: 企业（工厂）索引表
 *@Author: Lin Lee
 *@Date: 2019/5/10 15:28
 */
@Document(indexName = "enterprise", type = "_doc")
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
    @Field(type = FieldType.Text,analyzer="ik_max_word", searchAnalyzer="ik_max_word", store = true)
    @ScriptedField(name = "name")
    private String name;

    @Field(type = FieldType.Text,analyzer="ik_max_word", searchAnalyzer="ik_max_word", store = true)
    private String brand;

    @Field(type= FieldType.Object, store = true)
    private List<ProductToEnterpriseIndex> products;

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

    public List<ProductToEnterpriseIndex> getProducts() {
        return products;
    }

    public void setProducts(List<ProductToEnterpriseIndex> products) {
        this.products = products;
    }
}
