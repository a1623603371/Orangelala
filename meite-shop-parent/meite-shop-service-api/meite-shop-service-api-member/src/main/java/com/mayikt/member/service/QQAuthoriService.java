package com.mayikt.member.service;

import com.alibaba.fastjson.JSONObject;
import com.mayikt.bese.BaseResponse;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ProjectName: Orangelala
 * @Package: com.mayikt.member.service
 * @ClassName: QQAuthoriService
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/6/30 0:30
 * @Version: 1.0
 */
@Api(tags = "qq联合登入接口")
public interface QQAuthoriService {
    /**
     * 根据 openid查询是否已经绑定,如果已经绑定，则直接实现自动登陆
     *
     * @param qqOpenId
     * @return
     */
    @RequestMapping("/findByOpenId")
    BaseResponse<JSONObject> findByOpenId(@RequestParam("qqOpenId") String qqOpenId);

}
