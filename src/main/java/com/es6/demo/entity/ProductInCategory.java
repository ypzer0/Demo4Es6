package com.es6.demo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

/**
 * @Author: yangpeng
 * @ClassName: todo
 * @Description: todo
 * @Date: 2020/8/24 19:49
 * @Version v1.0
 */
public class ProductInCategory {
    /***
     * PS
     * FieldType.Keyword：不做分词的string类型
     * FieldType.Text：做分词的string类型
     */
    @Id
    @Field(type= FieldType.Keyword, store = true)
    private String id;
    /**产品名称*/
    @Field(type = FieldType.Text,analyzer="ik_smart", searchAnalyzer="ik_smart", store = true)
    private String name;
    /**
     * 产品参数
     */
    @Field(type = FieldType.Text,analyzer="ik_smart", searchAnalyzer="ik_smart", store = true)
    private String parameter;

    /**
     * 简介
     */
    @Field(type = FieldType.Text,analyzer="ik_smart", searchAnalyzer="ik_smart", store = true)
    private String introduction;

    /**产品所属公司名称*/
    @Field(type = FieldType.Text,analyzer="ik_smart", searchAnalyzer="ik_smart", store = true)
    private String companyName;
    /**
     * 产品所属公司品牌
     */
    @Field(type = FieldType.Text,analyzer="ik_smart", searchAnalyzer="ik_smart", store = true)
    private String companyBrand;
    /**
     * 产品所属公司介绍
     */
    @Field(type = FieldType.Text,analyzer="ik_smart", searchAnalyzer="ik_smart", store = true)
    private String companyIntroduction;

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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyBrand() {
        return companyBrand;
    }

    public void setCompanyBrand(String companyBrand) {
        this.companyBrand = companyBrand;
    }

    public String getCompanyIntroduction() {
        return companyIntroduction;
    }

    public void setCompanyIntroduction(String companyIntroduction) {
        this.companyIntroduction = companyIntroduction;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ProductInCategory) {
            ProductInCategory productInCategory = (ProductInCategory) obj;
            return id.equals(productInCategory.id);
        } else {
            throw new ClassCastException();
        }
    }
}
