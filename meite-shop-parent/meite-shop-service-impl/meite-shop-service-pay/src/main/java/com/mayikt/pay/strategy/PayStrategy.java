package com.mayikt.pay.strategy;

import com.mayikt.pay.mapper.entity.PaymentChannelEntity;
import com.mayikt.weixin.out.dto.PayMentTransacDTO;

/**
 * @ProjectName: Orangelala
 * @Package: com.mayikt.pay.strategy
 * @ClassName: PayStrategy
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/13 21:42
 * @Version: 1.0
 */
public interface PayStrategy {
    public String toPayHtml(PaymentChannelEntity pymentChannel, PayMentTransacDTO payMentTransacDTO);
}
