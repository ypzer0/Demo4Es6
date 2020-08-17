package com.es6.demo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.ScriptedField;

import java.util.List;

/**
 * @Author: yangpeng
 * @ClassName: ProductToEnterpriseIndex
 * @Description: todo
 * @Date: 2020/8/13 8:56
 * @Version v1.0
 */
public class ProductToEnterpriseIndex {
    @Id
    @Field(type= FieldType.Keyword,index =false, store = true)
    private String id;
    @Field(type= FieldType.Keyword,store = true)
    private String enterpriseId;
    @Field(type= FieldType.Object,store = true)
    private EnterpriseIndex enterpriseInfo;
    /**产品名称*/
    @Field(type = FieldType.Text,analyzer="ik_max_word", searchAnalyzer="ik_max_word", store = true)
    @ScriptedField(name = "name")
    private String name;
    /**价格*/
    @Field(type= FieldType.Double,index =true, store = true)
    private Double price;

    @Field(type= FieldType.Object, store = true)
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
