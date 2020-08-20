package com.es6.demo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: yangpeng
 * @ClassName: InnerHits
 * @Description: 标识搜索结果中的InnerHits字符串
 * @Date: 2020/8/19 17:55
 * @Version v1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface InnerHits {
    /**
     * 填写内部类的全类名
     */
    String name() default "";
    /**
     * 内部类在主类中的字段名
     */
    String fieldName() default "";

}
