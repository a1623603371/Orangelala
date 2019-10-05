package com.mayikt.auth.serivce.api;

import com.alibaba.fastjson.JSONObject;
import com.mayikt.bese.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ProjectName: 用户授权接口
 * @Package: com.mayikt.auth.serivce.api
 * @ClassName: AuthorizationService
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/9/1 14:09
 * @Version: 1.0
 */
public interface AuthorizationService {
    /**
     * 机构申请appid，appsecret
     */
    @GetMapping("applyAppInfo")
    public BaseResponse<JSONObject> applyAppInfo(@RequestParam("") String appName);
    /**
     * 使用appid于appsectet获取AccessToken
     *
     */
    @GetMapping("getAccessToken")
    public BaseResponse<JSONObject> getAccessToken(@RequestParam("appid") String appid,@RequestParam("appsecret") String appsecret);
    /**
     *验证token是否失效
     */
    public BaseResponse<JSONObject> getAppInfo(@RequestParam("accessToken") String accessToken);

}
