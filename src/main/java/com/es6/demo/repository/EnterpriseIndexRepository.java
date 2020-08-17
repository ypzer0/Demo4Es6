package com.es6.demo.repository;

import com.es6.demo.entity.EnterpriseIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: yangpeng
 * @ClassName: todo
 * @Description: todo
 * @Date: 2020/8/13 8:52
 * @Version v1.0
 */
@Repository
public interface EnterpriseIndexRepository extends ElasticsearchRepository<EnterpriseIndex,String> {
}