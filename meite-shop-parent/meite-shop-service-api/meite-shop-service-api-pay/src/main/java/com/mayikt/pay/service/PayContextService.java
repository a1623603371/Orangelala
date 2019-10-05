package com.mayikt.pay.service;

import com.alibaba.fastjson.JSONObject;
import com.mayikt.bese.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ProjectName: 根据不同的渠道id(支付方式) 返回不同的支付提交表单
 * @Package: com.mayikt.pay.service
 * @ClassName: PayContextService
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/13 21:29
 * @Version: 1.0
 */
public interface PayContextService {
    /**
     *
     * @param channelId
     * @param payToken
     * @return
     */
    @GetMapping("/payHtml")
    public BaseResponse<JSONObject> payHtml(@RequestParam("channelId")String channelId,@RequestParam("payToken")  String payToken);

}
