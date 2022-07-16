package indi.springboot.rabbitmq.config;

/*
 *  回调  发布确认（高级）
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class MyCallBack implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnsCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        //注入
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    /*
     *  交换机确认回调分发
     *  1.交换机接收到后回调
     *      1.1.correlationData：消息的相关信息
     *      1.2.b 交换机收到消息 true
     *      1.3.null
     *  2.交换机接收失败
     *      2.1.correlationData：消息的相关信息
     *      2.2.b 交换机 未 收到消息 false
     *      2.3.失败原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {
        String id = correlationData != null ? correlationData.getId() : "";
        if (b) {
            log.info("交换机已收到ID为：{}的消息", id);
        }
        else {
            log.info("交换机未收到ID为：{}的消息,由于：{}", id, s);
        }
    }

    //可以在 当消息传递过程中 不可达 目的地 时 将消息 返回 给 生产者
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        log.error("消息{}，被交换机{}退回，原因：{}，路由Key：{}",
                new String(returnedMessage.getMessage().getBody()),
                returnedMessage.getExchange(),
                returnedMessage.getReplyText(),
                returnedMessage.getRoutingKey());
    }
}
