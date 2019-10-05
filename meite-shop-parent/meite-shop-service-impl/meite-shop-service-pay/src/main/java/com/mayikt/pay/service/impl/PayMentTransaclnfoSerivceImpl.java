package com.mayikt.pay.service.impl;

import com.mayikt.bean.MeiteBeanUtils;
import com.mayikt.bese.BaseApiService;
import com.mayikt.bese.BaseResponse;
import com.mayikt.bese.GenerateToken;
import com.mayikt.core.bean.MiteBeanUtils;
import com.mayikt.pay.mapper.PaymentTransactionMapper;
import com.mayikt.pay.mapper.entity.PaymentTransactionEntity;
import com.mayikt.pay.service.PayMentTransacInfoService;
import com.mayikt.weixin.out.dto.PayMentTransacDTO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ProjectName: Orangelala
 * @Package: com.mayikt.pay.service.impl
 * @ClassName: PayMentTransaclnfoSerivceImpl
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/9 19:47
 * @Version: 1.0
 */
@RestController
public class PayMentTransaclnfoSerivceImpl extends BaseApiService<PayMentTransacDTO> implements PayMentTransacInfoService {
    @Autowired
    private GenerateToken generateToken;
    @Autowired
    private PaymentTransactionMapper paymentTransactionMapper;

    @Override
    public BaseResponse<PayMentTransacDTO> tokenByPayMentTransac(String token) {
        //验证token是否为空
        if (StringUtils.isEmpty(token)){
            return setResultError("token参数不能空!");
        }
        //使用token查询redisPayMentTransacDTO
        String vlue=generateToken.getToken(token);
        if (StringUtils.isEmpty(vlue)){
            return  setResultError("该token可能失效或已过期");
        }
        //3.转换为整数类型
        Long transactionId=Long.parseLong(vlue);
        //4.使用transactionId查询支付信息
        PaymentTransactionEntity paymentTransactionEntity=paymentTransactionMapper.selectById(transactionId);
        if (paymentTransactionEntity==null){
            setResultError("没有查询到该支付信息");
        }

        return setResultSuccess1(paymentTransactionEntity);
    }
}
