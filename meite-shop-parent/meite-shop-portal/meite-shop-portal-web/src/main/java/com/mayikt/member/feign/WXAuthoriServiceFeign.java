package com.mayikt.member.feign;

import com.mayikt.member.service.WeixinAuthoriService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @ProjectName: Orangelala
 * @Package: com.mayikt.member.feign
 * @ClassName: WXAuthoriServiceFeign
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/1 17:24
 * @Version: 1.0
 */
@FeignClient("app-mayikt-member")
public interface WXAuthoriServiceFeign extends WeixinAuthoriService {

}
