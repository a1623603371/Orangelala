package com.mayikt.zuul.feign;


import com.mayikt.auth.serivce.api.AuthorizationService;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @ProjectName: Orangelala
 * @Package: com.mayikt.zuul.feign
 * @ClassName: AuthorizationServiceFeign
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/9/1 15:48
 * @Version: 1.0
 */
@FeignClient("app-mayikt-auth")
public interface AuthorizationServiceFeign extends AuthorizationService {

}
