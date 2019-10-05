package com.mayikt.pay.mq.producer;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ProjectName: 生产者投替积分
 * @Package: com.mayikt.pay.mq.producer
 * @ClassName: IntegralProducer
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/24 17:41
 * @Version: 1.0
 */
@Component
@Slf4j
public class IntegralProducer implements RabbitTemplate.ConfirmCallback{
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Transactional
    public void  send(JSONObject jsonObject){
        String jsonString=jsonObject.toJSONString();
        System.out.println("jonsString"+jsonString);
        String paymentId=jsonObject.getString("paymentId");
        //封装消息
        Message message= MessageBuilder.withBody(jsonString.getBytes()).setContentType(MessageProperties.CONTENT_TYPE_JSON).setContentEncoding("utf-8").setMessageId(paymentId)
                .build();
        //构造回调返回数据（消息id）
        this.rabbitTemplate.setMandatory(true);
        this.rabbitTemplate.setConfirmCallback(this);
        CorrelationData correlationData=new CorrelationData(jsonString);
        rabbitTemplate.convertAndSend("integral_exchange_name", "integralRoutingKey", message, correlationData);
    }
//生产消息确认机制 生产往服务器端使用应达机制
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String josnString =correlationData.getId();
        System.out.println("消息ID"+correlationData.getId());
        if (ack){
            log.info(">>> 使用mq消息机制确保消息一定要投替到Mq中");
        }
        JSONObject jsonObject=JSONObject.parseObject( josnString);
            log.info(">>>使用MQ确认机制投替到mq失败");

    }
}
