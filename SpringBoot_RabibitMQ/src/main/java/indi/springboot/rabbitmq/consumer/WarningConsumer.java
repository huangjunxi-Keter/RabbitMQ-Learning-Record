package indi.springboot.rabbitmq.consumer;

/*
 *  报警消费者
 */

import indi.springboot.rabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class WarningConsumer {

    //接收报警消息
    @RabbitListener(queues = ConfirmConfig.WARNING_QUEUE_NAME)
    public void receiveWarningMsg(Message message) {
        String msg = new String(message.getBody());
        log.info("warning 发现不可路由消息：{}", msg);
    }

}
