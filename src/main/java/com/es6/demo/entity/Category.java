package com.es6.demo.entity;

import com.es6.demo.annotation.InnerHits;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;
import java.util.Set;

/**
 * @Author: yangpeng
 * @ClassName: todo
 * @Description: todo
 * @Date: 2020/8/13 15:11
 * @Version v1.0
 */
@Document(indexName = "category",type = "_doc")
public class Category {
    @Id
    @Field(type= FieldType.Keyword,index =false, store = true)
    private String id;

    @Field(type = FieldType.Keyword)
    private String keyword;

    /**目录名称*/
    @Field(type = FieldType.Text,analyzer="ik_smart", searchAnalyzer="ik_smart", store = true)
    private String name;

    @Field(type= FieldType.Keyword,index =false, store = true)
    private String parentId;

    @Field(type = FieldType.Text,analyzer="ik_max_word", searchAnalyzer="ik_max_word", store = true)
    private String parentName;

    @Field(type = FieldType.Text,store = true)
    private Set<String> enterpriseIds;

    @Field(type = FieldType.Long,store = true)
    private Long clickNum;

    @Field(type = FieldType.Nested,store = true)
    private Set<ProductInCategory> products;

    @InnerHits(name = "com.es6.demo.entity.ProductInCategory",fieldName = "products")
    private List<ProductInCategory> innerHits;


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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Set<String> getEnterpriseIds() {
        return enterpriseIds;
    }

    public void setEnterpriseIds(Set<String> enterpriseIds) {
        this.enterpriseIds = enterpriseIds;
    }

    public Long getClickNum() {
        return clickNum;
    }

    public void setClickNum(Long clickNum) {
        this.clickNum = clickNum;
    }

    public Set<ProductInCategory> getProducts() {
        return products;
    }

    public void setProducts(Set<ProductInCategory> products) {
        this.products = products;
    }

    public List<ProductInCategory> getInnerHits() {
        return innerHits;
    }

    public void setInnerHits(List<ProductInCategory> innerHits) {
        this.innerHits = innerHits;
    }
}
