package com.es6.demo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @Author: yangpeng
 * @ClassName: todo
 * @Description: todo
 * @Date: 2020/8/13 11:55
 * @Version v1.0
 */
public class MyJoinField {
    @Field(type = FieldType.Text)
    private String name;
    @Field(type = FieldType.Text)
    private String parent;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

}
