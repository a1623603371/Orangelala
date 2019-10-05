package com.mayikt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * @ProjectName: Orangelala
 * @Package: com.mayikt.product
 * @ClassName: AppProduct
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/28 16:55
 * @Version: 1.0
 */
@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = { "com.mayikt.product.es" })
public class AppProduct {
    public static void main(String[] args) {
        SpringApplication.run(AppProduct.class,args);

    }
}


