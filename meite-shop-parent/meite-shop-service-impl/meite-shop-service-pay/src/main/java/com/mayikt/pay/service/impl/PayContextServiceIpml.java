package com.mayikt.pay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mayikt.bese.BaseApiService;
import com.mayikt.bese.BaseResponse;
import com.mayikt.pay.factory.StrataegyFactory;
import com.mayikt.pay.mapper.PaymentChannelMapper;
import com.mayikt.pay.mapper.entity.PaymentChannelEntity;
import com.mayikt.pay.service.PayContextService;
import com.mayikt.pay.strategy.PayStrategy;
import com.mayikt.weixin.out.dto.PayMentTransacDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ProjectName: Orangelala
 * @Package: com.mayikt.pay.service.impl
 * @ClassName: PayContextServiceIpml
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/13 21:32
 * @Version: 1.0
 */
@RestController
public class PayContextServiceIpml extends BaseApiService<JSONObject> implements PayContextService {
    @Autowired
    private PaymentChannelMapper paymentChannelMapper;
    @Autowired
    private PayMentTransaclnfoSerivceImpl payMentTransaclnfoSerivce;

    /**
     * @param channelId
     * @param payToken
     * @return
     */
    @Override
    public BaseResponse<JSONObject> payHtml(String channelId, String payToken) {
        //使用渠道id获取信息，classAdders
        PaymentChannelEntity paymentChannel=paymentChannelMapper.selectBychannelId(channelId);
        if (paymentChannel==null){
            setResultError("没有查询到该渠道id");
        }
        //2.使用paytoken获取支付信息
        BaseResponse<PayMentTransacDTO> tokenByPayMentTransac=payMentTransaclnfoSerivce.tokenByPayMentTransac(payToken);
        if (!isSuccess(tokenByPayMentTransac)){
            return setResultError(tokenByPayMentTransac.getMsg());
        }
        PayMentTransacDTO payMentTransacDTO=tokenByPayMentTransac.getData();
        //3.执行具体的支付渠道的算法和html表单数据策略模式，使用java反射机制 执行具体方法
        String classAddes=paymentChannel.getClassAddres();
        PayStrategy payStrategy= StrataegyFactory.getPayStrategy(classAddes);
        String payHtml=payStrategy.toPayHtml(paymentChannel,payMentTransacDTO);
        //4.直接返回html
        JSONObject data=new JSONObject();
        data.put("payHtml",payHtml);
        return setResultSuccess(data);
    }
}
