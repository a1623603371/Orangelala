package com.xxl.sso.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author xuxueli 2018-03-22 23:41:47
 */
@SpringBootApplication(scanBasePackages = {"com.mayikt","com.xxl.sso"})
@EnableEurekaClient
@EnableFeignClients
@MapperScan("com.xxl.sso.server.mapper")
public class XxlSsoServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(XxlSsoServerApplication.class, args);
	}

}