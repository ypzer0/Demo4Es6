package com.es6.demo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.ScriptedField;

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

    /**目录名称*/
    @Field(type = FieldType.Keyword)
    //@ScriptedField(name = "name")
    private String name;

    @Field(type= FieldType.Keyword,index =false, store = true)
    private String parentId;

    @Field(type = FieldType.Text,analyzer="ik_max_word", searchAnalyzer="ik_max_word", store = true)
    private String parentName;

    @Field(type = FieldType.Text,store = true)
    private Set<String> enterpriseIds;

    @Override
    public String toString() {
        return "Category{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", parentId='" + parentId + '\'' +
                ", parentName='" + parentName + '\'' +
                ", enterpriseIds=" + enterpriseIds +
                '}';
    }

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
}
