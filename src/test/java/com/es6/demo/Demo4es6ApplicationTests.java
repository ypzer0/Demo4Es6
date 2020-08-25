package com.es6.demo;

import com.es6.demo.entity.*;
import com.es6.demo.mapper.CustomResultMapper;
import com.es6.demo.repository.CategoryIndexRepository;
import com.es6.demo.repository.EnterpriseIndexRepository;
import com.es6.demo.repository.ProductIndexRepository;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
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

import java.text.SimpleDateFormat;
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
     * 加入专卖供应商的产品
     */
    @Test
    void addSingleProduct() {
        ProductIndex productIndex = new ProductIndex();
        EnterpriseIndex enterpriseIndex = enterpriseIndexRepository.findById("AKYfH3QBMuOJ6PqwxAlW").get();
        productIndex.setEnterpriseId(enterpriseIndex.getId());
        // 创建产品下的目录索引内容
        List<CategoryInProduct> categories = new ArrayList<>();
        String categoryId = "7";
        createCategories(categories, enterpriseIndex.getId(), categoryId);
        productIndex.setCategories(categories);
        productIndex.setName("测试交流泵");
        productIndex.setParameter("测试交流泵参数");
        productIndex.setIntroduction("测试交流泵简介");
        productIndexRepository.save(productIndex);
        List<ProductInEnterprise> products = enterpriseIndex.getProducts();
        if (CollectionUtils.isEmpty(products)) {
            products = new ArrayList<>();
        }
        ProductInEnterprise productInEnterprise = new ProductInEnterprise();
        BeanUtils.copyProperties(productIndex, productInEnterprise);
        products.add(productInEnterprise);
        enterpriseIndex.setProducts(products);
        enterpriseIndexRepository.save(enterpriseIndex);
        // 更新目录下的产品信息
        Optional<Category> optionalCategory = categoryIndexRepository.findById(categoryId);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            Set<ProductInCategory> productsInCategory = category.getProducts();
            if (CollectionUtils.isEmpty(productsInCategory)) {
                productsInCategory = new HashSet<>();
            }
            ProductInCategory productInCategory = new ProductInCategory();
            BeanUtils.copyProperties(productIndex,productInCategory);
            productInCategory.setCompanyBrand(enterpriseIndex.getBrand());
            productInCategory.setCompanyIntroduction(enterpriseIndex.getIntroduction());
            productsInCategory.add(productInCategory);
            category.setProducts(productsInCategory);
            categoryIndexRepository.save(category);
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
        int addNum = 30;
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
            productIndex.setParameter(productName + "参数信息" + i);
            productIndex.setEnterpriseName(enterpriseIndex.getName());
            productIndex.setIntroduction(productName + "简介信息" + i);
            productIndex.setCreateTime(new Date());
            productIndexRepository.save(productIndex);
            List<ProductInEnterprise> products = enterpriseIndex.getProducts();
            if (CollectionUtils.isEmpty(products)) {
                products = new ArrayList<>();
            }
            ProductInEnterprise productInEnterprise = new ProductInEnterprise();
            BeanUtils.copyProperties(productIndex, productInEnterprise);
            products.add(productInEnterprise);
            enterpriseIndex.setProducts(products);
            Set<String> categoryIds = enterpriseIndex.getCategoryIds();
            if (CollectionUtils.isEmpty(categoryIds)) {
                categoryIds = new HashSet<>();
            }
            categoryIds.add(categoryId);
            enterpriseIndex.setCategoryIds(categoryIds);
            enterpriseIndexRepository.save(enterpriseIndex);
            // 更新目录下的产品信息
            Optional<Category> optionalCategory = categoryIndexRepository.findById(categoryId);
            if (optionalCategory.isPresent()) {
                Category category = optionalCategory.get();
                Set<ProductInCategory> productsInCategory = category.getProducts();
                if (CollectionUtils.isEmpty(productsInCategory)) {
                    productsInCategory = new HashSet<>();
                }
                ProductInCategory productInCategory = new ProductInCategory();
                BeanUtils.copyProperties(productIndex,productInCategory);
                productInCategory.setCompanyBrand(enterpriseIndex.getBrand());
                productInCategory.setCompanyIntroduction(enterpriseIndex.getIntroduction());
                productsInCategory.add(productInCategory);
                category.setProducts(productsInCategory);
                categoryIndexRepository.save(category);
            }
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
        List<ProductInEnterprise> products = enterpriseIndex.getProducts();
        if (CollectionUtils.isEmpty(products)) {
            products = new ArrayList<>();
        }
        ProductInEnterprise productInEnterprise = new ProductInEnterprise();
        BeanUtils.copyProperties(productIndex, productInEnterprise);
        products.add(productInEnterprise);
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


    /**
     * 多条件组合检索目录
     */
    @Test
    void searchCategory() {
        // 设前端传入的目录是keyword
        String keyword = "交流电机";
        MatchQueryBuilder query1 = QueryBuilders.matchQuery("name", keyword).boost(10.0f);
        MatchQueryBuilder matchQuery1 = QueryBuilders.matchQuery("products.name", keyword).boost(5.0f);
        MatchQueryBuilder matchQuery2 = QueryBuilders.matchQuery("products.parameter", keyword).boost(5.0f);
        MatchQueryBuilder matchQuery3 = QueryBuilders.matchQuery("products.introduction", keyword).boost(5.0f);
        MatchQueryBuilder matchQuery4 = QueryBuilders.matchQuery("products.companyName", keyword).boost(2.5f);
        MatchQueryBuilder matchQuery5 = QueryBuilders.matchQuery("products.companyBrand", keyword).boost(2.5f);
        MatchQueryBuilder matchQuery6 = QueryBuilders.matchQuery("products.companyIntroduction", keyword).boost(2.5f);
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.should(matchQuery1)
                .should(matchQuery2).should(matchQuery3)
                .should(matchQuery4).should(matchQuery5)
                .should(matchQuery6);
        NestedQueryBuilder query2 = QueryBuilders.nestedQuery("products", boolQuery, ScoreMode.Total);
        query2.boost(5.0f);
        InnerHitBuilder innerHitBuilder = new InnerHitBuilder();
        // 返回匹配的前一百个(引擎最多支持100个)
        innerHitBuilder.setSize(100);
        HighlightBuilder highlightBuilder2 = new HighlightBuilder();
        // 产品高亮字段
        highlightBuilder2.field("products.name");
        // 产品高亮字段
        highlightBuilder2.field("products.parameter");
        // 产品高亮字段
        highlightBuilder2.field("products.introduction");
        // 产品高亮字段
        highlightBuilder2.field("products.companyName");
        // 产品高亮字段
        highlightBuilder2.field("products.companyBrand");
        // 产品高亮字段
        highlightBuilder2.field("products.companyIntroduction");
        // 高亮标签
        highlightBuilder2.preTags("<span style='color:red;font-weight:700;'>").postTags("</span>");
        innerHitBuilder.setHighlightBuilder(highlightBuilder2);
        query2.innerHit(innerHitBuilder);
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.should(query1).should(query2);
        //设置分页
        nativeSearchQueryBuilder.withQuery(boolQueryBuilder);
        ScoreSortBuilder scoreSortBuilder = SortBuilders.scoreSort();
        FieldSortBuilder sortBuilder = SortBuilders.fieldSort("clickNum").order(SortOrder.DESC);
        nativeSearchQueryBuilder.withSort(scoreSortBuilder).withSort(sortBuilder);
        SearchQuery searchQuery = nativeSearchQueryBuilder.build();
        CustomResultMapper customResultMapper = new CustomResultMapper();
        AggregatedPage<Category> result = elasticsearchTemplate.queryForPage(searchQuery, Category.class, customResultMapper);
        System.out.println("搜索" + keyword + ":");
        for (Category category : result) {
            Set<String> enterpriseIds = category.getEnterpriseIds();
            System.out.println("{");
            System.out.println("   目录名称:" + category.getName());
            System.out.println("   包含:" + enterpriseIds.size() + "家企业");
            List<ProductInCategory> innerHits = category.getInnerHits();
            for (ProductInCategory innerHit : innerHits) {
                System.out.println("     {");
                System.out.println("        产品名称:" + innerHit.getName());
                System.out.println("        产品参数:" + innerHit.getParameter());
                System.out.println("        产品简介:" + innerHit.getIntroduction());
                System.out.println("        公司名称:" + innerHit.getCompanyName());
                System.out.println("        公司品牌:" + innerHit.getCompanyBrand());
                System.out.println("        公司简介:" + innerHit.getCompanyIntroduction());
                System.out.println("     }");
            }
            System.out.println("}");
        }
    }

    @Test
    void addProduct() {
        List<ProductIndex> list = new ArrayList<>(50);
        for (int i = 0; i < 50; i++) {
            ProductIndex productIndex = new ProductIndex();
            productIndex.setIntroduction("交流泵");
            list.add(productIndex);
        }
        productIndexRepository.saveAll(list);
    }

    /**
     * 精确匹配到分类，后多条件组合查询所属企业
     */
    @Test
    void searchEnterprisePrecise() {
        String keyword = "交流泵";
        MatchQueryBuilder categoryQuery = QueryBuilders.matchQuery("keyword", keyword);
        Category category = categoryIndexRepository.search(categoryQuery).iterator().next();
        if (!Optional.ofNullable(category).isPresent()) {
            System.out.println("关键字未匹配到目录");
            return;
        }
        MatchQueryBuilder query = QueryBuilders.matchQuery("categoryIds", category.getId());
        query.boost(20.0f);
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
                .must(query)
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
        ScoreSortBuilder scoreSortBuilder = SortBuilders.scoreSort();
        FieldSortBuilder sortBuilder = SortBuilders.fieldSort("createTime").order(SortOrder.DESC);
        // 一定要设置分数排序
        nativeSearchQueryBuilder.withSort(scoreSortBuilder).withSort(sortBuilder);
        SearchQuery searchQuery = nativeSearchQueryBuilder.build();
        CustomResultMapper customResultMapper = new CustomResultMapper();
        AggregatedPage<EnterpriseIndex> result = elasticsearchTemplate.queryForPage(searchQuery, EnterpriseIndex.class, customResultMapper);
        System.out.println("搜索" + keyword +":");
        for (EnterpriseIndex enterpriseIndex : result) {
            StringBuilder stringBuilder = new StringBuilder();
            List<ProductInEnterprise> products = enterpriseIndex.getInnerHits();
            for (ProductInEnterprise product : products) {
                stringBuilder.append(product.getName()).append(" ");
            }
            System.out.println("{");
            System.out.println("       供应商名称:" + enterpriseIndex.getName());
            System.out.println("       供应商品牌:" + enterpriseIndex.getBrand());
            System.out.println("       供应商简介:" + enterpriseIndex.getIntroduction());
            String productString = stringBuilder.toString();
            if (!StringUtils.isEmpty(productString)) {
                System.out.println("       供应商产品:" + productString);
            }
            System.out.println("}");
        }
    }

    /**
     * 多条件组合查询所属企业
     */
    @Test
    void searchEnterprise() {
        // 设前端传入的目录是keyword
        String keyword = "交流泵";
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
        ScoreSortBuilder scoreSortBuilder = SortBuilders.scoreSort();
        FieldSortBuilder sortBuilder = SortBuilders.fieldSort("createTime").order(SortOrder.DESC);
        // 一定要设置分数排序
        nativeSearchQueryBuilder.withSort(scoreSortBuilder).withSort(sortBuilder);
        SearchQuery searchQuery = nativeSearchQueryBuilder.build();
        CustomResultMapper customResultMapper = new CustomResultMapper();
        AggregatedPage<EnterpriseIndex> result = elasticsearchTemplate.queryForPage(searchQuery, EnterpriseIndex.class, customResultMapper);
        System.out.println("搜索" + keyword +":");
        for (EnterpriseIndex enterpriseIndex : result) {
            StringBuilder stringBuilder = new StringBuilder();
            List<ProductInEnterprise> products = enterpriseIndex.getInnerHits();
            for (ProductInEnterprise product : products) {
                stringBuilder.append(product.getName()).append(" ");
            }
            System.out.println("{");
            System.out.println("       供应商名称:" + enterpriseIndex.getName());
            System.out.println("       供应商品牌:" + enterpriseIndex.getBrand());
            System.out.println("       供应商简介:" + enterpriseIndex.getIntroduction());
            String productString = stringBuilder.toString();
            if (!StringUtils.isEmpty(productString)) {
                System.out.println("       供应商产品:" + productString);
            }
            System.out.println("}");
        }
    }

    /**
     * 多条件组合搜索产品
     */
    @Test
    void searchProduct() {
        // 设前端传入的目录是keyword
        String keyword = "交流泵";
        MatchQueryBuilder query1 = QueryBuilders.matchQuery("name", keyword);
        query1.boost(10.0f);
        MatchQueryBuilder query2 = QueryBuilders.matchQuery("parameter", keyword);
        query2.boost(7.5f);
        MatchQueryBuilder query3 = QueryBuilders.matchQuery("enterpriseName", keyword);
        query3.boost(5.0f);
        MatchQueryBuilder query4 = QueryBuilders.matchQuery("introduction", keyword);
        query4.boost(2.5f);
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.should(query1)
                .should(query2)
                .should(query3)
                .should(query4);
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        //设置分页
        PageRequest dataPage = PageRequest.of(0, 1000);
        nativeSearchQueryBuilder.withPageable(dataPage);
        nativeSearchQueryBuilder.withQuery(boolQuery);
        ScoreSortBuilder scoreSortBuilder = SortBuilders.scoreSort();
        FieldSortBuilder sortBuilder = SortBuilders.fieldSort("createTime").order(SortOrder.DESC);
        // 一定要设置分数排序
        nativeSearchQueryBuilder.withSort(scoreSortBuilder).withSort(sortBuilder);
        NativeSearchQuery searchQuery = nativeSearchQueryBuilder.build();
        Page<ProductIndex> page = productIndexRepository.search(searchQuery);
        List<ProductIndex> result = page.getContent();
        System.out.println("搜索" + keyword +",分类匹配的产品是:");
        for (ProductIndex productIndex : result) {
            System.out.println("{");
            System.out.println("  产品名称: " + productIndex.getName());
            System.out.println("  产品参数: " + productIndex.getParameter());
            System.out.println("  产品企业: " + productIndex.getEnterpriseName());
            System.out.println("  产品介绍: " + productIndex.getIntroduction());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (Optional.ofNullable(productIndex.getCreateTime()).isPresent()) {
                System.out.println("  创建时间: " + formatter.format(productIndex.getCreateTime()) );
            }
            System.out.println("}");
        }
    }

}
