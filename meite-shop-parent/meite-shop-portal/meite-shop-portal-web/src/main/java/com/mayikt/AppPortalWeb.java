package com.mayikt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @ProjectName: PortalWEb
 * @Package: com.mayikt
 * @ClassName: AppPortalWeb
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/6/28 14:40
 * @Version: 1.0
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
@EnableEurekaClient
@EnableFeignClients
public class AppPortalWeb {
    public static void main(String[] args) {
        SpringApplication.run(AppPortalWeb.class,args);
    }
}
