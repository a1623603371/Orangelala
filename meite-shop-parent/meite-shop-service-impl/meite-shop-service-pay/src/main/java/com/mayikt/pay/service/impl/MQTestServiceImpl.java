package com.mayikt.pay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mayikt.pay.mq.producer.IntegralProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ProjectName: 生产者测试消息
 * @Package: com.mayikt.pay.service.impl
 * @ClassName: MQTestServiceImpl
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/24 21:38
 * @Version: 1.0
 */
@RestController
public class MQTestServiceImpl {
    @Autowired
    private IntegralProducer integralProducer;
    @RequestMapping("/send")
    public String send() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("paymentId", System.currentTimeMillis());
        jsonObject.put("userId", "123456");
        jsonObject.put("integral", 100);
        integralProducer.send(jsonObject);
        return "success";
    }
}
