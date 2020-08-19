package com.es6.demo.mapper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.data.elasticsearch.core.ResultsExtractor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: yangpeng
 * @ClassName: InnerResultsExtractor
 * @Description: 获取嵌套查询中子文档的Extractor,返回结果为主文档id与子文档集合组成的map
 * @Date: 2020/8/18 18:28
 * @Version v1.0
 */
public class InnerResultsExtractor<T> implements ResultsExtractor<Map<String,List<T>>> {

    private Class<T> clazz;

    public InnerResultsExtractor(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Map<String,List<T>> extract(SearchResponse searchResponse) {
        SearchHit[] hits = searchResponse.getHits().getHits();
        List<T> innerHitList;
        Map<String,List<T>> resultMap = new HashMap<>(8);
        for (SearchHit hit : hits) {
            Map<String, SearchHits> innerHits = hit.getInnerHits();
            innerHitList = new ArrayList<>();
            for (Map.Entry<String, SearchHits> entry : innerHits.entrySet()) {
                for (SearchHit documentFields : entry.getValue().getHits()) {
                    String sourceAsString = documentFields.getSourceAsString();
                    JSONObject jsonObject = JSON.parseObject(sourceAsString);
                    T t = jsonObject.toJavaObject(clazz);
                    innerHitList.add(t);
                }
            }
            resultMap.put(hit.getId(),innerHitList);
        }
        return resultMap;
    }
}
