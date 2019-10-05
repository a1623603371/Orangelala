package com.mayikt.product.api.service.impl;

import com.mayikt.bese.BaseApiService;
import com.mayikt.bese.BaseResponse;
import com.mayikt.member.output.dto.ProductDto;
import com.mayikt.product.api.service.ProductSearchService;
import com.mayikt.product.es.entity.ProductEntity;
import com.mayikt.product.es.reposiory.ProductRepository;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ProjectName: Orangelala
 * @Package: com.mayikt.product.api.serivce.impl
 * @ClassName: ProductSearchServiceImpl
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/28 17:02
 * @Version: 1.0
 */
@RestController
public class ProductSearchServiceImpl extends BaseApiService<List<ProductDto>> implements ProductSearchService{
    @Autowired
    private ProductRepository productRepository;
    @Override
    public BaseResponse<List<ProductDto>> search(String name) {
        //1.拼接查询条件
        BoolQueryBuilder builder= QueryBuilders.boolQuery();
        //2.模糊查询name字段
        builder.must(QueryBuilders.multiMatchQuery(name,"name","subtitle","detail"));
        Pageable pageable=new QPageRequest(0,5);
        //3.调用es接口查询
        Page<ProductEntity> page=productRepository.search(builder,pageable);
        //4.获取集合数据
        List<ProductEntity> content=page.getContent();
        //5.将entity装换成dto
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        List<ProductDto> mapAsList=mapperFactory.getMapperFacade().mapAsList(content,ProductDto.class);

        return setResultSuccess(mapAsList);    }
}
