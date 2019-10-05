package com.mayikt.pay.service;

import com.mayikt.bese.BaseResponse;
import com.mayikt.weixin.input.dto.PayCratePayTokenDto;
import com.mayikt.weixin.out.dto.PayMentTransacDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ProjectName: 支付交易服务接口
 * @Package: com.mayikt.pay.service
 * @ClassName: PayMentTransacInfoService
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/9 12:33
 * @Version: 1.0
 */
public interface PayMentTransacInfoService {
    /**
     * 使用token查询支付信息
     * @param token
     * @return
     */
    @GetMapping("/tokenByPayMentTransac")
    public BaseResponse<PayMentTransacDTO> tokenByPayMentTransac(@RequestParam("token") String token);


}
