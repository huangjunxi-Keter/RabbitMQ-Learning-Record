package indi.springboot.rabbitmq.config;

/*
 *
 */

import org.springframework.amqp.core.*;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class DelayedQueueConfig {

    // 交换机
    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
    // 队列
    public static final String DELAYED_QUEUE_NAME = "delayed.queue";
    // RoutingKey
    public static final String DELAYED_ROUTING_KEY = "delayed.routingKey";

    //声明交换机 基于插件
    @Bean
    public CustomExchange delayedExchange() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-delayed-type","direct");
        /*
         *  1.交换机的名称
         *  2.交换机的类型
         *  3.是否需要持久化
         *  4.是否需要自动删除
         *  5.其他的参数
         */
        return new CustomExchange(DELAYED_EXCHANGE_NAME, "x-delayed-message", true, false, arguments);
    }

    //声明队列
    @Bean
    public Queue delayedQueue() {
        return new Queue(DELAYED_QUEUE_NAME);
    }

    //绑定
    @Bean
    public Binding delayedQueueBindingDelayedExchange(@Qualifier("delayedQueue") Queue delayedQueue,
                                                      @Qualifier("delayedExchange") CustomExchange delayedExchange) {
        return BindingBuilder.bind(delayedQueue).to(delayedExchange).with(DELAYED_ROUTING_KEY).noargs();
    }

}
