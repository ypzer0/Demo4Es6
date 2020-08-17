package com.es6.demo.repository;

import com.es6.demo.entity.ProductIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: yangpeng
 * @ClassName: ProductIndexRepository
 * @Description: todo
 * @Date: 2020/8/13 9:52
 * @Version v1.0
 */
@Repository
public interface ProductIndexRepository extends ElasticsearchRepository<ProductIndex,String> {
}
