package com.mayikt.pay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mayikt.bese.BaseApiService;
import com.mayikt.bese.BaseResponse;
import com.mayikt.bese.GenerateToken;
import com.mayikt.pay.mapper.PaymentTransactionMapper;
import com.mayikt.pay.mapper.entity.PaymentTransactionEntity;
import com.mayikt.pay.service.PayMentTransacTokenService;
import com.mayikt.twitter.SonwflakeldUtils;
import com.mayikt.weixin.input.dto.PayCratePayTokenDto;
import com.mayikt.weixin.out.dto.PayMentTransacDTO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ProjectName: Orangelala
 * @Package: com.mayikt.pay.service.impl
 * @ClassName: PayMentTransacTokenServiceImpl
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/9 21:51
 * @Version: 1.0
 */
@RestController
public class PayMentTransacTokenServiceImpl extends BaseApiService<JSONObject> implements PayMentTransacTokenService {
    @Autowired
    private PaymentTransactionMapper paymentTransactionMapper;
    @Autowired
    private GenerateToken generateToken;

    /**
     * 创建支付令牌
     *
     * @param payCratePayTokenDto
     * @return
     */
    @Override
    public BaseResponse<JSONObject> cartePayToken(PayCratePayTokenDto payCratePayTokenDto) {
        String orderId=payCratePayTokenDto.getOrderId();
        if (StringUtils.isEmpty(orderId)){
            return setResultError("订单号不能为空");
        }
        Long payAmount=payCratePayTokenDto.getPayAmount();
        if (payAmount==null){
            return  setResultError("金额不能为空");
        }
        Long userId = payCratePayTokenDto.getUserId();
        if (userId == null) {
            return setResultError("userId不能为空!");
        }
        //将输入插入数据库中，待支付记录
        PaymentTransactionEntity paymentTransactionEntity=new PaymentTransactionEntity();
        paymentTransactionEntity.setUserId(userId);
        paymentTransactionEntity.setPayAmount(payAmount);
        paymentTransactionEntity.setOrderId(orderId);
        String paymentId=SonwflakeldUtils.nextId();
        //使用全局id生成唯一id
        paymentTransactionEntity.setPaymentId(paymentId);
        System.out.println(paymentId);
        int  result=paymentTransactionMapper.insertPaymentTransaction(paymentTransactionEntity);
        if (!toDaoResult(result)) {
            return setResultError("系统错误");
        }
        //获取主键id
      Long payId=paymentTransactionEntity.getId();
        //3.生成支付令牌
        String keyPrefix="pay_";
        String token=generateToken.createToken(keyPrefix,payId+"");
        JSONObject dataRsult=new JSONObject();
            dataRsult.put("token",token);
        return setResultSuccess(dataRsult);
    }
}
