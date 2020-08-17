package com.es6.demo;

import com.es6.demo.entity.Category;
import com.es6.demo.entity.CategoryInProduct;
import com.es6.demo.entity.EnterpriseIndex;
import com.es6.demo.entity.ProductIndex;
import com.es6.demo.repository.CategoryIndexRepository;
import com.es6.demo.repository.EnterpriseIndexRepository;
import com.es6.demo.repository.ProductIndexRepository;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
        enterpriseIndex.setId("1");
        enterpriseIndex.setBrand("品牌1");
        enterpriseIndex.setName("供应商1");
        enterpriseIndexRepository.save(enterpriseIndex);
        enterpriseIndex.setId("2");
        enterpriseIndex.setBrand("品牌2");
        enterpriseIndex.setName("供应商2");
        enterpriseIndexRepository.save(enterpriseIndex);
        enterpriseIndex.setId("3");
        enterpriseIndex.setBrand("品牌3");
        enterpriseIndex.setName("供应商3");
        enterpriseIndexRepository.save(enterpriseIndex);
    }

    /**
     * 模拟保存电机产品
     */
    @Test
    void saveElectricMachinery() {
        ProductIndex productIndex = new ProductIndex();
        // 若保存的是一号测试企业，id为1
        EnterpriseIndex enterpriseIndex = enterpriseIndexRepository.findById("1").get();
        productIndex.setEnterpriseId(enterpriseIndex.getId());
        //productIndex.setEnterpriseInfo(enterpriseIndex);
        // 直流电机目录id是3
        String categoryId = "4";
        // 利用set去重特性保证目录下企业id不重复
        // 创建产品下的目录索引内容
        List<CategoryInProduct> categories = new ArrayList<>();
        createCategories(categories, enterpriseIndex.getId(), categoryId);
        productIndex.setCategories(categories);
        productIndex.setName("交流电机一号");
        productIndexRepository.save(productIndex);
        List<CategoryInProduct> categoriesInEnterprise = enterpriseIndex.getCategories();
        if (CollectionUtils.isEmpty(categoriesInEnterprise)) {
            categoriesInEnterprise = new ArrayList<>();
        }
        categoriesInEnterprise.addAll(categories);
        enterpriseIndex.setCategories(categoriesInEnterprise);
        enterpriseIndexRepository.save(enterpriseIndex);
    }

    /**
     * 模拟保存泵
     */
    @Test
    void savePump() {
        ProductIndex productIndex = new ProductIndex();
        // 保存在三号企业下
        EnterpriseIndex enterpriseIndex = enterpriseIndexRepository.findById("3").get();
        productIndex.setEnterpriseId(enterpriseIndex.getId());
        //productIndex.setEnterpriseInfo(enterpriseIndex);
        // 水泵分类id是6
        String categoryId = "7";
        // 利用set去重特性保证目录下企业id不重复
        // 创建产品下的目录索引内容
        List<CategoryInProduct> categories = new ArrayList<>();
        createCategories(categories, enterpriseIndex.getId(), categoryId);
        productIndex.setCategories(categories);
        productIndex.setName("交流泵一号");
        productIndexRepository.save(productIndex);
        List<CategoryInProduct> categoriesInEnterprise = enterpriseIndex.getCategories();
        if (CollectionUtils.isEmpty(categoriesInEnterprise)) {
            categoriesInEnterprise = new ArrayList<>();
        }
        categoriesInEnterprise.addAll(categories);
        enterpriseIndex.setCategories(categoriesInEnterprise);
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
        String keyword = "*" + "交流电机" +"*";
        WildcardQueryBuilder query = QueryBuilders.wildcardQuery("name", keyword);
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
    void searchEnterpriseByCategory() {
        // 设前端传入的目录是level
        String keyword = "交流电机";
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("categories.name", keyword);
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        //设置分页
        nativeSearchQueryBuilder.withQuery(termQueryBuilder);
        SearchQuery searchQuery = nativeSearchQueryBuilder.build();
        Page<EnterpriseIndex> page = enterpriseIndexRepository.search(searchQuery);
        List<EnterpriseIndex> result = page.getContent();
        System.out.println("搜索" + keyword +",旗下产品分类匹配的供应商是:");
        for (EnterpriseIndex enterpriseIndex : result) {
            System.out.println(enterpriseIndex.getName());
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
