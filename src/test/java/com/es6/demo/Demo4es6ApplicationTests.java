package com.es6.demo;

import com.es6.demo.entity.*;
import com.es6.demo.mapper.CustomResultMapper;
import com.es6.demo.repository.CategoryIndexRepository;
import com.es6.demo.repository.EnterpriseIndexRepository;
import com.es6.demo.repository.ProductIndexRepository;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

@SpringBootTest
class Demo4es6ApplicationTests {
    @Autowired
    private EnterpriseIndexRepository enterpriseIndexRepository;
    @Autowired
    private ProductIndexRepository productIndexRepository;
    @Autowired
    private CategoryIndexRepository categoryIndexRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;


    /**
     * 创建分类
     */
    @Test
    void createElectricMachineryCategory() {
        Category category1 = new Category();
        category1.setId("1");
        category1.setName("顶级分类");
        categoryIndexRepository.save(category1);
        Category category2 = new Category();
        category2.setId("2");
        category2.setName("电机");
        category2.setParentId(category1.getId());
        categoryIndexRepository.save(category2);
        Category category3 = new Category();
        category3.setId("3");
        category3.setParentId(category2.getId());
        category3.setName("直流电机");
        categoryIndexRepository.save(category3);
        Category category4 = new Category();
        category4.setId("4");
        category4.setParentId(category2.getId());
        category4.setName("交流电机");
        categoryIndexRepository.save(category4);
        Category category5 = new Category();
        category5.setId("5");
        category5.setName("泵");
        category5.setParentId("1");
        categoryIndexRepository.save(category5);
        Category category6 = new Category();
        category6.setId("6");
        category6.setParentId(category5.getId());
        category6.setName("直流泵");
        categoryIndexRepository.save(category6);
        Category category7 = new Category();
        category7.setId("7");
        category7.setParentId(category5.getId());
        category7.setName("交流泵");
        categoryIndexRepository.save(category7);
    }


    @Test
    void saveEnterprise() {
        EnterpriseIndex enterpriseIndex = new EnterpriseIndex();
        for (int i = 1; i <= 10; i++) {
            enterpriseIndex.setId( "" + i);
            enterpriseIndex.setBrand("品牌" + i);
            enterpriseIndex.setName("供应商" + i);
            enterpriseIndexRepository.save(enterpriseIndex);
        }

    }

    /**
     * 模拟保存产品
     */
    @Test
    void saveElectricMachinery() {
        // todo 直流电机-3 交流电机-4 直流泵-6 交流泵-7
        createEMData("3");
        createEMData("4");
        createEMData("6");
        createEMData("7");
    }

    private void createEMData(String categoryId) {
        ProductIndex productIndex = new ProductIndex();
        int addNum = 50;
        for (int i = 1; i <= addNum; i++) {
            // 随机找一个企业id
            String randomId = String.valueOf((int) (1 + Math.random() * (10)));
            EnterpriseIndex enterpriseIndex = enterpriseIndexRepository.findById(randomId).get();
            productIndex.setEnterpriseId(enterpriseIndex.getId());
            // 创建产品下的目录索引内容
            List<CategoryInProduct> categories = new ArrayList<>();
            createCategories(categories, enterpriseIndex.getId(), categoryId);
            productIndex.setCategories(categories);
            String productName = "";
            switch (categoryId) {
                case "3":
                    productName = "直流电机";
                    productIndex.setId("" + i);
                    break;
                case "4":
                    productName = "交流电机";
                    productIndex.setId("" + i + addNum);
                    break;
                case "6":
                    productName = "直流泵";
                    productIndex.setId("" + i + addNum * 2);
                    break;
                case "7":
                    productName = "交流泵";
                    productIndex.setId("" + i + addNum * 3);
                    break;
            }
            productIndex.setName(productName + i + "号");
            productIndexRepository.save(productIndex);
            List<ProductToEnterpriseIndex> products = enterpriseIndex.getProducts();
            if (CollectionUtils.isEmpty(products)) {
                products = new ArrayList<>();
            }
            ProductToEnterpriseIndex productToEnterpriseIndex = new ProductToEnterpriseIndex();
            BeanUtils.copyProperties(productIndex,productToEnterpriseIndex);
            products.add(productToEnterpriseIndex);
            enterpriseIndex.setProducts(products);
            enterpriseIndexRepository.save(enterpriseIndex);
        }
    }

    private void createCategories(List<CategoryInProduct> categories,String enterpriseId,String id) {
        // 3级直流电机的id为3
        Category category = categoryIndexRepository.findById(id).get();
        Set<String> enterpriseIds = category.getEnterpriseIds();
        if (CollectionUtils.isEmpty(enterpriseIds)) {
            enterpriseIds = new HashSet<>();
        }
        enterpriseIds.add(enterpriseId);
        category.setEnterpriseIds(enterpriseIds);
        categoryIndexRepository.save(category);
        CategoryInProduct categoryInProduct = new CategoryInProduct();
        BeanUtils.copyProperties(category,categoryInProduct);
        categories.add(categoryInProduct);
        String parentId = category.getParentId();
        if (StringUtils.isEmpty(parentId)) {
            return;
        }
        createCategories(categories, enterpriseId,parentId);
    }

    @Test
    void searchCategoryAndEnterpriseNum() {
        // 设前端传入的目录是keyword
        String keyword = "直流泵";
        MatchQueryBuilder query = QueryBuilders.matchQuery("name", keyword);
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        //设置分页
        nativeSearchQueryBuilder.withQuery(query);
        SearchQuery searchQuery = nativeSearchQueryBuilder.build();
        Page<Category> page = categoryIndexRepository.search(searchQuery);
        List<Category> result = page.getContent();
        System.out.println("搜索" + keyword + ":");
        for (Category category : result) {
            Set<String> enterpriseIds = category.getEnterpriseIds();
            StringBuilder stringBuilder = new StringBuilder();
            if (!CollectionUtils.isEmpty(enterpriseIds)) {
                stringBuilder.append("所属供应商分别是: ");
                for (String enterpriseId : enterpriseIds) {
                    stringBuilder.append(enterpriseIndexRepository.findById(enterpriseId).get().getName()).append(" ");
                }
                System.out.println(category.getName() + "    " + enterpriseIds.size() + "家供应商," + stringBuilder.toString());
            }
        }
    }



    @Test
    void searchEnterpriseByProduct() {
        // 设前端传入的目录是keyword
        String keyword = "直流泵";
        MatchQueryBuilder query = QueryBuilders.matchQuery("products.name", keyword);
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        //设置分页
        nativeSearchQueryBuilder.withPageable(PageRequest.of(0,10));
        nativeSearchQueryBuilder.withQuery(query);
        SearchQuery searchQuery = nativeSearchQueryBuilder.build();
        Page<EnterpriseIndex> page = enterpriseIndexRepository.search(searchQuery);
        List<EnterpriseIndex> result = page.getContent();
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
        }
    }

    @Test
    void searchProductByCategory() {
        // 设前端传入的目录是keyword
        String keyword = "交流电机";
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("categories.name", keyword);
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        //设置分页
        PageRequest dataPage = PageRequest.of(0, 50);
        nativeSearchQueryBuilder.withPageable(dataPage);
        nativeSearchQueryBuilder.withQuery(termQueryBuilder);
        SearchQuery searchQuery = nativeSearchQueryBuilder.build();
        Page<ProductIndex> page = productIndexRepository.search(searchQuery);
        List<ProductIndex> result = page.getContent();
        System.out.println("搜索" + keyword +",分类匹配的产品是:");
        for (ProductIndex productIndex : result) {
            System.out.println(productIndex.getName());
        }
    }


}