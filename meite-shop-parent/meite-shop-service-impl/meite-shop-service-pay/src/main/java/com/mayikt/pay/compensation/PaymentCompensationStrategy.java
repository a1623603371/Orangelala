package com.mayikt.pay.compensation;

import com.mayikt.pay.mapper.entity.PaymentChannelEntity;
import com.mayikt.pay.mapper.entity.PaymentTransactionEntity;

/**
 * @ProjectName: Orangelala
 * @Package: com.mayikt.pay.compensation
 * @ClassName: PyamentCompensationStrategy
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/24 14:50
 * @Version: 1.0
 */
public interface PaymentCompensationStrategy {
    /**
     *
     *手动补偿
     */
    public Boolean payMentCompensation(PaymentTransactionEntity paymentTransactionEntity, PaymentChannelEntity paymentChannelEntity);


}
