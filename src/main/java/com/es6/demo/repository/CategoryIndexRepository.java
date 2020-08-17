package com.es6.demo.repository;

import com.es6.demo.entity.Category;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CategoryIndexRepository extends ElasticsearchRepository<Category,String> {
}
