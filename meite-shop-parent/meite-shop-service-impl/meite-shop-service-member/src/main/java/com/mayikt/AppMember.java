package com.mayikt;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.spring4all.swagger.EnableSwagger2Doc;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
* @Description:    会员服务
* @Author:         yc
* @CreateDate:     2019/6/16 16:07
* @UpdateUser:     yc
* @UpdateDate:     2019/6/16 16:07
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableSwagger2Doc
@EnableApolloConfig
//@ComponentScan(basePackages = {"com.mayikt.member.service"})
@MapperScan("com.mayikt.member.mapper")
public class AppMember {
    public static void main(String[] args) {
        SpringApplication.run(AppMember.class,args);
    }
}
