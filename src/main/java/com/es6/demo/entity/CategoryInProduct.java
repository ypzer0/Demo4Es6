package com.es6.demo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @Author: yangpeng
 * @ClassName: todo
 * @Description: todo
 * @Date: 2020/8/14 9:35
 * @Version v1.0
 */
public class CategoryInProduct {
    @Id
    @Field(type= FieldType.Keyword,index =false, store = true)
    private String id;

    /**目录名称*/
    @Field(type = FieldType.Keyword)
    // @ScriptedField(name = "name")
    private String name;

    @Field(type= FieldType.Keyword,index =false, store = true)
    private String parentId;

    @Field(type = FieldType.Text,analyzer="ik_max_word", searchAnalyzer="ik_max_word", store = true)
    private String parentName;

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

    @Override
    public String toString() {
        return "CategoryInProduct{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", parentId='" + parentId + '\'' +
                ", parentName='" + parentName + '\'' +
                '}';
    }
}
