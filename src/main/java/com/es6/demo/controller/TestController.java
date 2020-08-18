package com.es6.demo.controller;

import com.es6.demo.entity.EnterpriseIndex;
import com.es6.demo.entity.ProductIndex;
import com.es6.demo.mapper.CustomResultMapper;
import com.es6.demo.repository.CategoryIndexRepository;
import com.es6.demo.repository.EnterpriseIndexRepository;
import com.es6.demo.repository.ProductIndexRepository;
import com.es6.demo.vo.EnterpriseVo;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: yangpeng
 * @ClassName: todo
 * @Description: todo
 * @Date: 2020/8/18 11:17
 * @Version v1.0
 */
@RestController
@RequestMapping(value = "/test")
public class TestController {
    @Autowired
    private EnterpriseIndexRepository enterpriseIndexRepository;
    @Autowired
    private ProductIndexRepository productIndexRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;


    @GetMapping
    public List<EnterpriseVo> getEnterprise(@RequestParam("keyword") String keyword) {
        MatchQueryBuilder query = QueryBuilders.matchQuery("products.name", keyword);
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        //设置分页
        nativeSearchQueryBuilder.withPageable(PageRequest.of(0,10));
        nativeSearchQueryBuilder.withQuery(query);
        SearchQuery searchQuery = nativeSearchQueryBuilder.build();
        Page<EnterpriseIndex> page = enterpriseIndexRepository.search(searchQuery);
        List<EnterpriseIndex> result = page.getContent();
        List<EnterpriseVo> resultList = new ArrayList<>();
        System.out.println("搜索" + keyword +",拥有该产品的供应商是:");
        for (EnterpriseIndex enterpriseIndex : result) {
            NativeSearchQueryBuilder productSearch = new NativeSearchQueryBuilder();
            MatchQueryBuilder query1 = QueryBuilders.matchQuery("enterpriseId", enterpriseIndex.getId());
            MatchQueryBuilder query2 = QueryBuilders.matchQuery("name", keyword);
            BoolQueryBuilder productQuery = QueryBuilders.boolQuery();
            productQuery.must(query1).must(query2);
            productSearch.withQuery(productQuery);
            //高亮规则定义
            HighlightBuilder highlightBuilder=new HighlightBuilder();
            highlightBuilder.preTags("<span style='color:red;font-weight:700;'>");
            highlightBuilder.postTags("</span>");
            //指定高亮字段
            highlightBuilder.field("name");
            productSearch.withHighlightBuilder(highlightBuilder);
            productSearch.withHighlightFields(
                    highlightBuilder.fields().get(0)
            );
            CustomResultMapper customResultMapper = new CustomResultMapper();
            AggregatedPage<ProductIndex> productIndexPage = elasticsearchTemplate.queryForPage(productSearch.build(), ProductIndex.class, customResultMapper);
            StringBuilder stringBuilder = new StringBuilder();
            List<ProductIndex> productIndexList = productIndexPage.getContent();
            for (ProductIndex productIndex : productIndexList) {
                stringBuilder.append(productIndex.getName()).append(" ");
            }
            System.out.println(enterpriseIndex.getName() + ",旗下"+ keyword +"有: " + stringBuilder.toString());
            EnterpriseVo enterpriseVo = new EnterpriseVo();
            enterpriseVo.setName(enterpriseIndex.getName());
            enterpriseVo.setProducts(productIndexList);
            resultList.add(enterpriseVo);
        }

        return resultList;
    }

}
