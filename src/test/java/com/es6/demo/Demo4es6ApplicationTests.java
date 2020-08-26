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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
        Category category = new Category();
        // 1.矿山专用设备
        category.setId("1");
        category.setName("矿山专用设备");
        category.setKeyword("矿山专用设备");
        categoryIndexRepository.save(category);
        //2.冶金专用设备
        category.setId("2");
        category.setName("冶金专用设备");
        category.setKeyword("冶金专用设备");
        categoryIndexRepository.save(category);
        //3.水泥专用设备
        category.setId("3");
        category.setName("水泥专用设备");
        category.setKeyword("水泥专用设备");
        categoryIndexRepository.save(category);
        // 24.建井设备 1
        category.setId("24");
        category.setName("建井设备");
        category.setKeyword("建井设备");
        category.setParentId("1");
        categoryIndexRepository.save(category);
        // 25.采掘、凿岩设备 1
        category.setId("25");
        category.setName("采掘、凿岩设备");
        category.setKeyword("采掘、凿岩设备");
        category.setParentId("1");
        categoryIndexRepository.save(category);
        // 26.矿山提升设备  1
        category.setId("26");
        category.setName("矿山提升设备");
        category.setKeyword("矿山提升设备");
        category.setParentId("1");
        categoryIndexRepository.save(category);
        // 27.矿物破碎机械  1
        category.setId("27");
        category.setName("矿物破碎机械");
        category.setKeyword("矿物破碎机械");
        category.setParentId("1");
        categoryIndexRepository.save(category);
        // 28.矿物粉磨机械  1
        category.setId("28");
        category.setName("矿物粉磨机械");
        category.setKeyword("矿物粉磨机械");
        category.setParentId("1");
        categoryIndexRepository.save(category);
        // 29.矿物筛分设备  1
        category.setId("29");
        category.setName("矿物筛分设备");
        category.setKeyword("矿物筛分设备");
        category.setParentId("1");
        categoryIndexRepository.save(category);
        // 36.炼铁设备 2
        category.setId("36");
        category.setName("炼铁设备");
        category.setKeyword("炼铁设备");
        category.setParentId("2");
        categoryIndexRepository.save(category);
        // 37.炼钢设备 2
        category.setId("37");
        category.setName("炼钢设备");
        category.setKeyword("炼钢设备");
        category.setParentId("2");
        categoryIndexRepository.save(category);
        // 65.化工专用炉 3
        category.setId("65");
        category.setName("化工专用炉");
        category.setKeyword("化工专用炉");
        category.setParentId("3");
        categoryIndexRepository.save(category);
        // 2582.钻井机  24
        category.setId("2582");
        category.setName("钻井机");
        category.setKeyword("钻井机");
        category.setParentId("24");
        categoryIndexRepository.save(category);
        // 2583.平巷掘进机  24
        category.setId("2583");
        category.setName("平巷掘进机");
        category.setKeyword("平巷掘进机");
        category.setParentId("24");
        categoryIndexRepository.save(category);
        // 2584.天井掘进设备  24
        category.setId("2584");
        category.setName("天井掘进设备");
        category.setKeyword("天井掘进设备");
        category.setParentId("24");
        categoryIndexRepository.save(category);
        // 2588.凿岩机  25
        category.setId("2588");
        category.setName("凿岩机");
        category.setKeyword("凿岩机");
        category.setParentId("25");
        categoryIndexRepository.save(category);
        // 2589.矿用钻车  25
        category.setId("2589");
        category.setName("矿用钻车");
        category.setKeyword("矿用钻车");
        category.setParentId("25");
        categoryIndexRepository.save(category);
        // 2590.钻孔设备（穿孔设备）  25
        category.setId("2590");
        category.setName("钻孔设备");
        category.setKeyword("钻孔设备");
        category.setParentId("25");
        categoryIndexRepository.save(category);
        // 2591.装药填充设备  25
        category.setId("2591");
        category.setName("装药填充设备");
        category.setKeyword("装药填充设备");
        category.setParentId("25");
        categoryIndexRepository.save(category);
        // 2628.砾磨机  28
        category.setId("2628");
        category.setName("砾磨机");
        category.setKeyword("砾磨机");
        category.setParentId("28");
        categoryIndexRepository.save(category);
        // 2629.管磨机  28
        category.setId("2629");
        category.setName("管磨机");
        category.setKeyword("管磨机");
        category.setParentId("28");
        categoryIndexRepository.save(category);
        // 2630.立式水泥磨机  28
        category.setId("2630");
        category.setName("立式水泥磨机");
        category.setKeyword("立式水泥磨机");
        category.setParentId("28");
        categoryIndexRepository.save(category);
        // 2631.其他矿物磨粉机械  28
        category.setId("2631");
        category.setName("其他矿物磨粉机械");
        category.setKeyword("其他矿物磨粉机械");
        category.setParentId("28");
        categoryIndexRepository.save(category);
    }


    @Test
    void saveEnterprise() {
        String enterpriseString = "浙江华源颜料,浙江三维橡胶,天津莱特,安徽华安,湖南浩森,浙江化工,江苏汇鸿集团,江苏海外,广州化工,山西晋迪鑫,山东昌乐永利塑料,河北环球,广西利土源农业,河北万达橡胶,山西三喜,江苏苏州海峰贸易,北京速牢克化工,福建展化化工,浙江-东海新材料,浙江四海,安徽中元化工集团,大连驹通,上海韬君,上海东贸,河南美德莱,内蒙古乐蒙石矿,河北中创,天津嘉泰丰,吉林化工进出口,宁夏昱辰,青岛传潮国际,天津天凯开米,安徽五矿发展,安徽中元化工集团,大连驹通,上海韬君,上海东贸,河南美德莱,内蒙古乐蒙石矿,内蒙古远鹏,广西博拉暨广顺,青岛金昊龙,山东东营广友橡胶,浙江龙圣华,深圳银鲲鹏,天津渤化化工,山东万和橡塑,河北荣达,河南金晟巍,山西晋迪鑫,浙江路霸工贸,上海亨憬工贸,宁夏平鑫炭素,天津海盛,大连凯兴国际,四川科立鑫,青岛锋泾集团,安徽格莱思曼,河南利浩,河北杰克,天津恩基欧,江西南都,青岛传潮国际,天津天凯开米,天津海盛,宁夏明迈特,江苏泰兴永盛,吉林化工进出口,广西贺州耀龙,山东物博润生,河南源波,山东启航,山东凯达,山东新兴化工,河南万泉,安徽格莱思曼,河南利浩,河北杰克,北京速牢克化工,山东雪原昌盛,福建展化化工,河南启培,湖北新都,浙江-东海新材料,江苏春环集团,浙江四海,湖南长沙仙山源,山西信实,上海一品颜料,广东中山大田,南京金腾橡塑,浙江德隆,辽宁抚顺新大陆,广东普赛达,河北海兴境美化工,青海洲华国际,河北一诺化工,湖北臻境,河北菁莱,河北昇恒科技,江西凯泰食品,江西金马活性炭,山东怀亮,四川金川磷化,河北凯发,安徽科贝瑞,宁波百富勤,广州卫斯理,河南五矿东方";
        String[] split = enterpriseString.split(",");
        List<EnterpriseIndex> list = new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            EnterpriseIndex enterpriseIndex = new EnterpriseIndex();
            enterpriseIndex.setId(String.valueOf((i+1)));
            enterpriseIndex.setName(split[i]);
            enterpriseIndex.setBrand(split[i]);
            enterpriseIndex.setIntroduction(split[i]);
            list.add(enterpriseIndex);
        }
        enterpriseIndexRepository.saveAll(list);
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
        String keyword = "水泥钻井机";
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
            if (!CollectionUtils.isEmpty(enterpriseIds)) {
                System.out.println("   包含:" + enterpriseIds.size() + "家企业");
            }
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

    @Test
    void changeE() {
        EnterpriseIndex enterpriseIndex = new EnterpriseIndex();
        enterpriseIndex.setId("91");
        enterpriseIndex.setName("南京金腾水泥");
        enterpriseIndex.setBrand("水泥水泥水泥水泥水泥水泥水泥水泥");
        enterpriseIndex.setIntroduction("水泥水泥水泥水泥");
        enterpriseIndexRepository.save(enterpriseIndex);
    }

    /**
     * 多条件组合查询所属企业
     */
    @Test
    void searchEnterprise() {
        // 设前端传入的目录是keyword
        String keyword = "河北万达橡胶";
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
