package com.mayikt.member.feign;

import com.mayikt.member.service.MemberExitService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @ProjectName: Orangelala
 * @Package: com.mayikt.member.feign
 * @ClassName: MemberExitServiceFeign
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/6/29 12:00
 * @Version: 1.0
 */
@FeignClient("app-mayikt-member")
public interface MemberExitServiceFeign extends MemberExitService {
}
