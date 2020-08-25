package com.es6.demo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.ScriptedField;

/**
 * @Author: yangpeng
 * @ClassName: ProductToEnterpriseIndex
 * @Description: todo
 * @Date: 2020/8/13 8:56
 * @Version v1.0
 */
public class ProductInEnterprise {
    @Id
    @Field(type= FieldType.Keyword,index =false, store = true)
    private String id;
    /**产品名称*/
    @Field(type = FieldType.Text,analyzer="ik_smart", searchAnalyzer="ik_smart", store = true)
    @ScriptedField(name = "name")
    private String name;

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

}
