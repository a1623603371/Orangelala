package com.mayikt.pay.feign;

import com.mayikt.pay.service.PayContextService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @ProjectName: Orangelala
 * @Package: com.mayikt.pay.feign
 * @ClassName: PayContextFeign
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/9/7 11:06
 * @Version: 1.0
 */
@FeignClient("app-mayikt-pay")
public interface PayContextFeign extends PayContextService {
}
