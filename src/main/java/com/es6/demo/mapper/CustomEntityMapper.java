package com.es6.demo.mapper;

import org.springframework.data.elasticsearch.core.DefaultEntityMapper;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentEntity;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentProperty;
import org.springframework.data.mapping.context.MappingContext;

import java.io.IOException;

/**
 * @Author: yangpeng
 * @ClassName: todo
 * @Description: todo
 * @Date: 2020/8/18 17:26
 * @Version v1.0
 */
public class CustomEntityMapper extends DefaultEntityMapper {

    private EntityMapper entityMapper;

    public CustomEntityMapper(MappingContext<? extends ElasticsearchPersistentEntity<?>, ElasticsearchPersistentProperty> context) {
        super(context);
    }

    @Override
    public <T> T mapToObject(String source, Class<T> clazz) throws IOException {
        return super.mapToObject(source, clazz);
    }

}
