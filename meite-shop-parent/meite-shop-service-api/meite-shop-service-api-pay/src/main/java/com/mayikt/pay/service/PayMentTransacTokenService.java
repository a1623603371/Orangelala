package com.mayikt.pay.service;

import com.alibaba.fastjson.JSONObject;
import com.mayikt.bese.BaseResponse;
import com.mayikt.weixin.input.dto.PayCratePayTokenDto;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @ProjectName: Orangelala
 * @Package: com.mayikt.pay.service
 * @ClassName: PayMentTransacTokenService
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/9 12:33
 * @Version: 1.0
 */
public interface  PayMentTransacTokenService {
    /**
     * 创建支付令牌
     * @param payCratePayTokenDto
     * @return
     */
    @GetMapping("/cartePayToken")
    public BaseResponse<JSONObject> cartePayToken(@Validated PayCratePayTokenDto payCratePayTokenDto);
}


