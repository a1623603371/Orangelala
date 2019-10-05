package com.mayikt.slike.mq.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @ProjectName: Orangelala
 * @Package: com.mayikt.slike.mq.config
 * @ClassName: RabbitmqConfig
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/9/3 21:36
 * @Version: 1.0
 */
@Component
public class RabbitmqConfig {
    // 添加修改库存队列
    public static final String MODIFY_INVENTORY_QUEUE = "modify_inventory_queue";
    // 交换机名称
    private static final String MODIFY_EXCHANGE_NAME = "modify_exchange_name";

    // 1.添加交换机队列
    @Bean
    public Queue directModifyInventoryQueue() {
        return new Queue(MODIFY_INVENTORY_QUEUE);
    }

    // 2.定义交换机
    @Bean
    DirectExchange directModifyExchange() {
        return new DirectExchange(MODIFY_EXCHANGE_NAME);
    }

    // 3.修改库存队列绑定交换机
    @Bean
    Binding bindingExchangeintegralDicQueue() {
        return BindingBuilder.bind(directModifyInventoryQueue()).to(directModifyExchange()).with("modifyRoutingKey");
    }


}
