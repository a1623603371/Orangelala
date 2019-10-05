package com.mayikt.member.feign;





import com.mayikt.weixin.service.WeiXinService;
import org.springframework.cloud.openfeign.FeignClient;

/**
* @Description:    调用微信服务接口
* @Author:         yc
* @CreateDate:     2019/6/16 15:49
* @UpdateUser:     yc
* @UpdateDate:     2019/6/16 15:49
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
@FeignClient("app-mayikt-weixin")
public interface WeiXinServiceFeign extends WeiXinService {

}
