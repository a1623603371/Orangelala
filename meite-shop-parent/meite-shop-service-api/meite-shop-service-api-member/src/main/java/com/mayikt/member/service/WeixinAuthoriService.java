package com.mayikt.member.service;

import com.alibaba.fastjson.JSONObject;
import com.mayikt.bese.BaseResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ProjectName: Orangelala
 * @Package: com.mayikt.member.service
 * @ClassName: WeixinAuthoriService
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/1 0:33
 * @Version: 1.0
 */
public interface WeixinAuthoriService {
    /**
     * 根据微信的openId查询用户信息
     * @param wxOpenId
     * @return
     */
    @RequestMapping("/findByOpenId")
    BaseResponse<JSONObject> findByOpenId(@RequestParam("wxOpenId") String wxOpenId);
}


