package com.mayikt.product.es.reposiory;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.mayikt.product.es.entity.ProductEntity;

public interface ProductRepository extends ElasticsearchRepository<ProductEntity, Long> {

}
 