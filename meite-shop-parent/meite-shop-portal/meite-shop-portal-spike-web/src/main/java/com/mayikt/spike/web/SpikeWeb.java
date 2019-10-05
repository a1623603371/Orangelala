package com.mayikt.spike.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

/**
 * @ProjectName: Orangelala
 * @Package: com.mayikt.spike.web
 * @ClassName: SpikeWeb
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/9/3 17:22
 * @Version: 1.0
 */
@SpringBootApplication (exclude = { DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
public class SpikeWeb {
    public static void main(String[] args) {
        SpringApplication.run(SpikeWeb.class,args);
    }
}
