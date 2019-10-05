package com.mayikt.slike.consumer;

import com.alibaba.fastjson.JSONObject;
import com.mayikt.slike.service.mapper.OrderMapper;
import com.mayikt.slike.service.mapper.SeckillMapper;
import com.mayikt.slike.service.mapper.entity.OrderEntity;
import com.mayikt.slike.service.mapper.entity.SeckillEntity;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @ProjectName: Orangelala
 * @Package: com.mayikt.slike.consumer
 * @ClassName: StockConsumer
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/9/3 21:51
 * @Version: 1.0
 */
@Component
@Slf4j
public class StockConsumer  {
    @Autowired
    private SeckillMapper seckillMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Transactional
    @RabbitListener(queues = "modify_inventory_queue")
    public void  process(Message message, @Headers Map<String, Object> headers, Channel channel) throws UnsupportedEncodingException {
        String messageId=message.getMessageProperties().getMessageId();
        String msg = new String(message.getBody(), "UTF-8");
        log.info(">>>messageId:{},msg:{}", messageId, msg);
        JSONObject jsonObject=JSONObject.parseObject(msg);
        //获取秒杀id
        Long seckillId=jsonObject.getLong("seckillId");
        SeckillEntity seckillEntity=seckillMapper.findBySeckillId(seckillId);
        if (seckillEntity==null){
            log.info("seckillId:{},商品信息不存在!", seckillId);
            return;
        }
        Long version = seckillEntity.getVersion();
        int inventoryDeduction = seckillMapper.inventoryDeduction(seckillId, version);
        if (!toDaoResult(inventoryDeduction)) {
            log.info(">>>seckillId:{}修改库存失败>>>>inventoryDeduction返回为{} 秒杀失败！", seckillId, inventoryDeduction);
            return;
        }
        // 2.添加秒杀订单
        OrderEntity orderEntity = new OrderEntity();
        String phone = jsonObject.getString("phone");
        orderEntity.setUserPhone(phone);
        orderEntity.setSeckillId(seckillId);
        orderEntity.setState(1l);
        int insertOrder = orderMapper.insertOrder(orderEntity);
        if (!toDaoResult(insertOrder)) {
            return;
        }
        log.info(">>>修改库存成功seckillId:{}>>>>inventoryDeduction返回为{} 秒杀成功", seckillId, inventoryDeduction);
    }

    // 调用数据库层判断
    public Boolean toDaoResult(int result) {
        return result > 0 ? true : false;
    }

    }

