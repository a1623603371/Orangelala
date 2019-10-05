package com.mayikt.member.feign;

import com.mayikt.weixin.service.VerificaCodeService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @ProjectName: Orangelala
 * @Package: com.mayikt.member.feign
 * @ClassName: VerificaCodeServiceFeign
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/6/25 16:38
 * @Version: 1.0
 */
@FeignClient("app-mayikt-weixin")
public interface VerificaCodeServiceFeign extends VerificaCodeService{
}
