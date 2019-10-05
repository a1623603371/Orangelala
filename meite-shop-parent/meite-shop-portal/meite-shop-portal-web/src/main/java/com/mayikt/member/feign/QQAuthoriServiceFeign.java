package com.mayikt.member.feign;

import com.mayikt.member.service.QQAuthoriService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @ProjectName: Orangelala
 * @Package: com.mayikt.member.feign
 * @ClassName: QQAuthoriService
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/6/30 0:41
 * @Version: 1.0
 */
@FeignClient("app-mayikt-member")
public interface QQAuthoriServiceFeign extends QQAuthoriService {
}
