package com.mayikt;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @ProjectName: Orangelala
 * @Package: com.mayikt
 * @ClassName: AppPay
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/9 14:34
 * @Version: 1.0
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableSwagger2Doc
// @EnableApolloConfig
@MapperScan(basePackages = "com.mayikt.pay.mapper")
public class AppPay {
    public static void main(String[] args) {
        SpringApplication.run(AppPay.class,args);
    }
}
