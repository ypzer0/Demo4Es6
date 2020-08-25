package com.es6.demo.mapper;

import com.alibaba.fastjson.JSON;
import com.es6.demo.annotation.InnerHits;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.document.DocumentField;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.ElasticsearchException;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.InnerField;
import org.springframework.data.elasticsearch.annotations.ScriptedField;
import org.springframework.data.elasticsearch.core.DefaultResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentEntity;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentProperty;
import org.springframework.data.elasticsearch.core.mapping.SimpleElasticsearchMappingContext;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.util.Assert;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.*;


/**
 * @Author: yangpeng
 * @ClassName: CustomResultMapper
 * @Description: todo
 * @Date: 2020/8/18 11:41
 * @Version v1.0
 */
public class CustomResultMapper extends DefaultResultMapper {

    private MappingContext<? extends ElasticsearchPersistentEntity<?>, ElasticsearchPersistentProperty> mappingContext = (MappingContext)(new SimpleElasticsearchMappingContext());

    private static final String SPOT = ".";

    @Override
    public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
        long totalHits = response.getHits().getTotalHits();
        float maxScore = response.getHits().getMaxScore();
        List<T> results = new ArrayList();
        Iterator var8 = response.getHits().iterator();

        while(var8.hasNext()) {
            SearchHit hit = (SearchHit)var8.next();
            if (hit != null) {
                T result = null;
                if (!org.springframework.util.StringUtils.isEmpty(hit.getSourceAsString())) {
                    result = this.mapEntity(hit.getSourceAsString(), clazz);
                } else {
                    result = this.mapEntity(hit.getFields().values(), clazz);
                }

                this.setPersistentEntityId(result, hit.getId(), clazz);
                this.setPersistentEntityVersion(result, hit.getVersion(), clazz);
                this.setPersistentEntityScore(result, hit.getScore(), clazz);
                //修复原版populateScriptFields漏洞
                this.populateScriptFields(result, hit);
                results.add(result);
            }
        }

        return new AggregatedPageImpl(results, pageable, totalHits, response.getAggregations(), response.getScrollId(), maxScore);
    }

    private <T> T mapEntity(Collection<DocumentField> values, Class<T> clazz) {
        return this.mapEntity(this.buildJSONFromFields(values), clazz);
    }

    private String buildJSONFromFields(Collection<DocumentField> values) {
        JsonFactory nodeFactory = new JsonFactory();

        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            JsonGenerator generator = nodeFactory.createGenerator(stream, JsonEncoding.UTF8);
            generator.writeStartObject();
            Iterator var5 = values.iterator();

            while(true) {
                while(var5.hasNext()) {
                    DocumentField value = (DocumentField)var5.next();
                    if (value.getValues().size() > 1) {
                        generator.writeArrayFieldStart(value.getName());
                        Iterator var7 = value.getValues().iterator();

                        while(var7.hasNext()) {
                            Object val = var7.next();
                            generator.writeObject(val);
                        }

                        generator.writeEndArray();
                    } else {
                        generator.writeObjectField(value.getName(), value.getValue());
                    }
                }

                generator.writeEndObject();
                generator.flush();
                return new String(stream.toByteArray(), Charset.forName("UTF-8"));
            }
        } catch (IOException var9) {
            return null;
        }
    }

    private <T> void setPersistentEntityId(T result, String id, Class<T> clazz) {
        if (clazz.isAnnotationPresent(Document.class)) {
            ElasticsearchPersistentEntity<?> persistentEntity = (ElasticsearchPersistentEntity)this.mappingContext.getRequiredPersistentEntity(clazz);
            ElasticsearchPersistentProperty idProperty = (ElasticsearchPersistentProperty)persistentEntity.getIdProperty();
            if (idProperty != null && idProperty.getType().isAssignableFrom(String.class)) {
                persistentEntity.getPropertyAccessor(result).setProperty(idProperty, id);
            }
        }

    }

    private <T> void setPersistentEntityVersion(T result, long version, Class<T> clazz) {
        if (clazz.isAnnotationPresent(Document.class)) {
            ElasticsearchPersistentEntity<?> persistentEntity = (ElasticsearchPersistentEntity)this.mappingContext.getPersistentEntity(clazz);
            ElasticsearchPersistentProperty versionProperty = persistentEntity.getVersionProperty();
            if (versionProperty != null && versionProperty.getType().isAssignableFrom(Long.class)) {
                Assert.isTrue(version != -1L, "Version in response is -1");
                persistentEntity.getPropertyAccessor(result).setProperty(versionProperty, version);
            }
        }

    }

    private <T> void setPersistentEntityScore(T result, float score, Class<T> clazz) {
        if (clazz.isAnnotationPresent(Document.class)) {
            ElasticsearchPersistentEntity<?> entity = (ElasticsearchPersistentEntity)this.mappingContext.getRequiredPersistentEntity(clazz);
            if (!entity.hasScoreProperty()) {
                return;
            }

            entity.getPropertyAccessor(result).setProperty(entity.getScoreProperty(), score);
        }

    }

    /**
     * @Author: yangpeng
     * @Description: 自定义解析innerHits与结果集高亮展示
     * @Date: 2020/8/19 16:46
     * @Param: result 查询结果的泛型
     * @Param: hit 查询结果
     * @Return: 操作结果
     * 修改人---修改日期---修改内容
     */
    private <T> void populateScriptFields(T result, SearchHit hit) {
        Map<String, SearchHits> innerHits = hit.getInnerHits();

        if(innerHits != null && innerHits.size() > 0) {
            createInnerHits(result,innerHits);
        }
        if (hit.getHighlightFields() != null && !hit.getHighlightFields().isEmpty() && result != null) {
            Field[] var3 = result.getClass().getDeclaredFields();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                Field field = var3[var5];
                ScriptedField scriptedField = (ScriptedField)field.getAnnotation(ScriptedField.class);
                if (scriptedField != null) {
                    String name = scriptedField.name().isEmpty() ? field.getName() : scriptedField.name();
                    HighlightField highlightField = hit.getHighlightFields().get(name);
                    if (highlightField != null) {
                        field.setAccessible(true);
                        try {
                            field.set(result, highlightField.getFragments()[0].toString());
                        } catch (IllegalArgumentException var11) {
                            throw new ElasticsearchException("failed to set scripted field: " + name + " with value: " + highlightField.getFragments()[0], var11);
                        } catch (IllegalAccessException var12) {
                            throw new ElasticsearchException("failed to access scripted field: " + name, var12);
                        }
                    }
                }
            }
        }

    }

    private <T> void createInnerHits(T result, Map<String, SearchHits> innerHits) {
        Field[] var3 = result.getClass().getDeclaredFields();
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Field field = var3[var5];
            InnerHits innerHitsAnno = field.getAnnotation(InnerHits.class);
            if (innerHitsAnno != null) {
                field.setAccessible(true);
                ArrayList<Object> list = new ArrayList<>();
                for (Map.Entry<String, SearchHits> hitsEntry : innerHits.entrySet()) {
                    for (SearchHit fields : hitsEntry.getValue().getHits()) {
                        Class<?> clazz = null;
                        try {
                            clazz = Class.forName(innerHitsAnno.name());
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        Object obj = null;
                        try {
                            assert clazz != null;
                            obj = clazz.newInstance();
                        } catch (InstantiationException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        Map<String, HighlightField> highlightFields = fields.getHighlightFields();
                        if (highlightFields.size() > 0) {
                            for (Map.Entry<String, HighlightField> entry : highlightFields.entrySet()) {
                                Field[] declaredFields = clazz.getDeclaredFields();
                                for (int i = 0; i < declaredFields.length; i++) {
                                    String fieldName = innerHitsAnno.fieldName() + SPOT + declaredFields[i].getName();
                                    if (fieldName.equals(entry.getKey())) {
                                        declaredFields[i].setAccessible(true);
                                        try {
                                            declaredFields[i].set(obj,entry.getValue().getFragments()[0].toString());
                                        } catch (IllegalAccessException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                        list.add(obj);
                        }
                    }
                try {
                    field.set(result, list);
                } catch (IllegalArgumentException var11) {
                    throw new ElasticsearchException("设置脚本字段失败");
                } catch (IllegalAccessException e) {
                    throw new ElasticsearchException("无法访问脚本字段");
                }
            }
        }
    }

}
