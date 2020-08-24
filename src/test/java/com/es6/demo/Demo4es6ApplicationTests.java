package com.es6.demo;

import com.es6.demo.entity.*;
import com.es6.demo.mapper.CustomResultMapper;
import com.es6.demo.repository.CategoryIndexRepository;
import com.es6.demo.repository.EnterpriseIndexRepository;
import com.es6.demo.repository.ProductIndexRepository;
import org.apache.lucene.search.join.ScoreMode;
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
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
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
        category1.setKeyword("顶级分类");
        categoryIndexRepository.save(category1);
        Category category2 = new Category();
        category2.setId("2");
        category2.setName("电机");
        category2.setKeyword("电机");
        category2.setParentId(category1.getId());
        categoryIndexRepository.save(category2);
        Category category3 = new Category();
        category3.setId("3");
        category3.setParentId(category2.getId());
        category3.setName("直流电机");
        category3.setKeyword("直流电机");
        categoryIndexRepository.save(category3);
        Category category4 = new Category();
        category4.setId("4");
        category4.setParentId(category2.getId());
        category4.setName("交流电机");
        category4.setKeyword("交流电机");
        categoryIndexRepository.save(category4);
        Category category5 = new Category();
        category5.setId("5");
        category5.setName("泵");
        category5.setKeyword("泵");
        category5.setParentId("1");
        categoryIndexRepository.save(category5);
        Category category6 = new Category();
        category6.setId("6");
        category6.setParentId(category5.getId());
        category6.setName("直流泵");
        category6.setKeyword("直流泵");
        categoryIndexRepository.save(category6);
        Category category7 = new Category();
        category7.setId("7");
        category7.setParentId(category5.getId());
        category7.setName("交流泵");
        category7.setKeyword("交流泵");
        categoryIndexRepository.save(category7);
    }

    @Test
    void saveSingleCategory() {
        Category category = new Category();
        category.setParentId("1");
        category.setKeyword("测试交流泵");
        category.setName("测试交流泵");
        categoryIndexRepository.save(category);
    }

    @Test
    void saveEnterprise() {
        for (int i = 1; i <= 10; i++) {
            EnterpriseIndex enterpriseIndex = new EnterpriseIndex();
            enterpriseIndex.setId( "" + i);
            enterpriseIndex.setName("供应商" + i);
            enterpriseIndexRepository.save(enterpriseIndex);
        }

        for (int i = 0; i < 7; i++) {
            EnterpriseIndex enterpriseIndex = new EnterpriseIndex();
            enterpriseIndex.setName("JOJO的奇妙冒险" + i);
            enterpriseIndex.setBrand("供应商品牌" + i);
            enterpriseIndex.setIntroduction("供应商简介" + i);
            enterpriseIndexRepository.save(enterpriseIndex);
        }
    }

    /**
     * 保存专卖供应商
     */
    @Test
    void saveSingleEnterprise() {
        EnterpriseIndex enterpriseIndex = new EnterpriseIndex();
        enterpriseIndex.setBrand("交流泵品牌");
        enterpriseIndex.setName("交流泵专卖商1号");
        enterpriseIndexRepository.save(enterpriseIndex);
    }

    /**
     * 加入专卖供应商的产品
     */
    @Test
    void addSingleProduct() {
        ProductIndex productIndex = new ProductIndex();
        EnterpriseIndex enterpriseIndex = enterpriseIndexRepository.findById("AKYfH3QBMuOJ6PqwxAlW").get();
        productIndex.setEnterpriseId(enterpriseIndex.getId());
        // 创建产品下的目录索引内容
        List<CategoryInProduct> categories = new ArrayList<>();
        createCategories(categories, enterpriseIndex.getId(), "7");
        productIndex.setCategories(categories);
        productIndex.setName("测试交流泵");
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
        int addNum = 300;
        for (int i = 1; i <= addNum; i++) {
            ProductIndex productIndex = new ProductIndex();
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
                    productName = "供应商直流电机";
                    productIndex.setId("" + i);
                    break;
                case "4":
                    productName = "供应商交流电机";
                    productIndex.setId("" + (i + addNum));
                    break;
                case "6":
                    productName = "供应商直流泵";
                    productIndex.setId("" + (i + addNum * 2));
                    break;
                case "7":
                    productName = "供应商交流泵";
                    productIndex.setId("" + (i + addNum * 3));
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

    @Test
    void addProductInEnterprise() {
        // 找一个企业id
        String id = "7";
        EnterpriseIndex enterpriseIndex = enterpriseIndexRepository.findById(id).get();
        ProductIndex productIndex = new ProductIndex();
        productIndex.setEnterpriseId(enterpriseIndex.getId());
        // 创建产品下的目录索引内容
        List<CategoryInProduct> categories = new ArrayList<>();
        // todo 直流电机-3 交流电机-4 直流泵-6 交流泵-7
        createCategories(categories, enterpriseIndex.getId(), "4");
        productIndex.setCategories(categories);
        productIndex.setName("特殊交流电机0号");
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
        String keyword = "测试交流泵";
        MatchQueryBuilder query1 = QueryBuilders.matchQuery("keyword", keyword).boost(10.f);
        MatchQueryBuilder query2 = QueryBuilders.matchQuery("name", keyword).boost(1.0f);
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.should(query1).should(query2);
        //设置分页
        nativeSearchQueryBuilder.withQuery(boolQueryBuilder);
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
                System.out.println(category.getName() + "  " + enterpriseIds.size() + "家供应商," + stringBuilder.toString());
            }
        }
    }

    @Test
    void searchEnterpriseByProductNested() {
        // 设前端传入的目录是keyword
        String keyword = "供应商";
        MatchQueryBuilder query1 = QueryBuilders.matchQuery("name", keyword);
        //高亮规则定义
        HighlightBuilder highlightBuilder=new HighlightBuilder();
        highlightBuilder.preTags("<span style='color:red;font-weight:700;'>");
        highlightBuilder.postTags("</span>");
        //指定高亮字段
        highlightBuilder.field("name");
        query1.boost(10.0f);
        MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("products.name", keyword);
        NestedQueryBuilder query2 = QueryBuilders.nestedQuery("products", matchQuery, ScoreMode.Total);
        query2.boost(5.0f);
        InnerHitBuilder innerHitBuilder = new InnerHitBuilder();
        // 返回匹配的前一百个(引擎最多支持100个)
        innerHitBuilder.setSize(100);
        HighlightBuilder highlightBuilder2 = new HighlightBuilder();
        // 产品高亮字段
        highlightBuilder2.field("products.name");
        // 高亮标签
        highlightBuilder2.preTags("<span style='color:red;font-weight:700;'>").postTags("</span>");
        innerHitBuilder.setHighlightBuilder(highlightBuilder2);
        query2.innerHit(innerHitBuilder);
        MatchQueryBuilder query3 = QueryBuilders.matchQuery("brand", keyword);
        query3.boost(1.0f);
        MatchQueryBuilder query4 = QueryBuilders.matchQuery("introduction", keyword);
        query4.boost(1.0f);
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .should(query1)
                .should(query2)
                .should(query3)
                .should(query4);
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        //设置分页
        nativeSearchQueryBuilder.withPageable(PageRequest.of(0,100));
        //设置企业类高亮
        nativeSearchQueryBuilder.withHighlightBuilder(highlightBuilder);
        nativeSearchQueryBuilder.withHighlightFields(
                highlightBuilder.fields().get(0)
        );
        nativeSearchQueryBuilder.withQuery(boolQuery);
        SearchQuery searchQuery = nativeSearchQueryBuilder.build();
        CustomResultMapper customResultMapper = new CustomResultMapper();
        AggregatedPage<EnterpriseIndex> result = elasticsearchTemplate.queryForPage(searchQuery, EnterpriseIndex.class, customResultMapper);
        System.out.println("搜索" + keyword +",拥有该产品的供应商是:");
        for (EnterpriseIndex enterpriseIndex : result) {
            StringBuilder stringBuilder = new StringBuilder();
            List<ProductToEnterpriseIndex> products = enterpriseIndex.getInnerHits();
            for (ProductToEnterpriseIndex product : products) {
                stringBuilder.append(product.getName()).append(" ");
            }
            System.out.println(enterpriseIndex.getName() +":" + ""  + ",旗下的产品有: " + stringBuilder.toString());
            System.out.println("{");
            System.out.println("供应商名称:" + enterpriseIndex.getName());
            System.out.println("供应商品牌:" + enterpriseIndex.getBrand());
            System.out.println("供应商简介:" + enterpriseIndex.getIntroduction());
            String productString = stringBuilder.toString();
            if (!StringUtils.isEmpty(productString)) {
                System.out.println("供应商产品:" + productString);
            }
            System.out.println("}");
        }
    }

    @Test
    void searchProductByCategory() {
        // 设前端传入的目录是keyword
        String keyword = "电机";
        MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("categories.name", keyword);
        NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("categories", matchQuery, ScoreMode.Total);
        nestedQuery.innerHit(new InnerHitBuilder());
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        //设置分页
        PageRequest dataPage = PageRequest.of(0, 2000);
        nativeSearchQueryBuilder.withPageable(dataPage);
        nativeSearchQueryBuilder.withQuery(nestedQuery);
        NativeSearchQuery searchQuery = nativeSearchQueryBuilder.build();
        Page<ProductIndex> page = productIndexRepository.search(searchQuery);
        List<ProductIndex> result = page.getContent();
        System.out.println("搜索" + keyword +",分类匹配的产品是:");
        for (ProductIndex productIndex : result) {
            System.out.println(productIndex.getName());
        }
    }


}
