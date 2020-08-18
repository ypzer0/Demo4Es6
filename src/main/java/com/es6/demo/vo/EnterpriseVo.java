package com.es6.demo.vo;

import com.es6.demo.entity.ProductIndex;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.ScriptedField;

import java.util.List;

/**
 * @Author: yangpeng
 * @ClassName: todo
 * @Description: todo
 * @Date: 2020/8/18 11:26
 * @Version v1.0
 */
public class EnterpriseVo {
    /**公司名称*/
    private String name;

    private List<ProductIndex> products;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProductIndex> getProducts() {
        return products;
    }

    public void setProducts(List<ProductIndex> products) {
        this.products = products;
    }
}
