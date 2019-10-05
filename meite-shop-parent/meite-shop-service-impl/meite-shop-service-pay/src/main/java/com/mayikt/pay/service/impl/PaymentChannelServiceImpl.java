package com.mayikt.pay.service.impl;

import com.mayikt.bese.BaseApiService;
import com.mayikt.mapper.MapperUtils;
import com.mayikt.pay.mapper.PaymentChannelMapper;
import com.mayikt.pay.mapper.entity.PaymentChannelEntity;
import com.mayikt.pay.service.PaymentChannelService;
import com.mayikt.weixin.out.dto.PaymentChannelDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ProjectName: Orangelala
 * @Package: com.mayikt.pay.service.impl
 * @ClassName: PaymentChannelServiceImpl
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/9 15:00
 * @Version: 1.0
 */
@RestController
public class PaymentChannelServiceImpl extends BaseApiService<List<PaymentChannelDTO>>
        implements PaymentChannelService {
    @Autowired
    private PaymentChannelMapper paymentChannelMapper;
    /**
     * 查询所有支付渠道
     */
    @Override
    public List<PaymentChannelDTO> selectAll() {
        List<PaymentChannelEntity> paymentChanneList = paymentChannelMapper.selectAll();
        return MapperUtils.mapAsList(paymentChanneList, PaymentChannelDTO.class);
    }



}
