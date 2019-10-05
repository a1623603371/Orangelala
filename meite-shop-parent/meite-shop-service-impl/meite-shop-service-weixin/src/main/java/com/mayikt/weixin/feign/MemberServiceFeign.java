package com.mayikt.weixin.feign;

import com.mayikt.member.service.MemberService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @ProjectName: 调用服务接口feign接口
 * @Package: com.mayitk.weixin.feign
 * @ClassName: MemberServiceFeign
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/6/25 13:25
 * @Version: 1.0
 */
@FeignClient("app-mayikt-member")
public interface MemberServiceFeign extends MemberService {
}
