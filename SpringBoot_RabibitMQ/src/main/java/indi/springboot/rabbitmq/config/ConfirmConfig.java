package indi.springboot.rabbitmq.config;

/*
 *  配置类 发布确认（高级）
 */

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfirmConfig {

    //交换机
    public static final String CONFIRM_EXCHANGE_NAME = "confirm.exchange";
    //队列
    public static final String CONFIRM_QUEUE_NAME = "confirm.queue";
    //RoutingKet
    public static final String CONFIRM_ROUTING_KEY = "key1";

    //备份交换机
    public static final String BACKUP_EXCHANGE_NAME = "backup.exchange";
    //备份队列
    public static final String BACKUP_QUEUE_NAME = "backup.queue";
    //报警队列
    public static final String WARNING_QUEUE_NAME = "backup.queue";


    //声明 交换机
    @Bean
    public DirectExchange confirmExchange() {
        return ExchangeBuilder
                .directExchange(CONFIRM_EXCHANGE_NAME)
                .durable(true)
                .withArgument("alternate-exchange", BACKUP_EXCHANGE_NAME)
                .build();
    }

    //声明 队列
    @Bean
    public Queue confirmQueue() {
        return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
    }

    //绑定
    @Bean
    public Binding queueBindingExchange(@Qualifier("confirmExchange") DirectExchange directExchange,
                                        @Qualifier("confirmQueue") Queue confirmQueue) {
        return BindingBuilder.bind(confirmQueue).to(directExchange).with(CONFIRM_ROUTING_KEY);
    }

    //声明    备份交换机
    @Bean
    public FanoutExchange backupExchange() {
        return new FanoutExchange(BACKUP_EXCHANGE_NAME);
    }

    //声明    备份队列
    @Bean
    public Queue backupQueue() {
        return QueueBuilder.durable(BACKUP_QUEUE_NAME).build();
    }

    //声明    报警队列
    @Bean
    public Queue warningQueue() {
        return QueueBuilder.durable(WARNING_QUEUE_NAME).build();
    }

    //绑定
    @Bean
    public Binding backupQueueBindingBackupExchange(@Qualifier("backupExchange") FanoutExchange backupExchange,
                                                    @Qualifier("backupQueue") Queue backupQueue) {
        return BindingBuilder.bind(backupQueue).to(backupExchange);
    }
    @Bean
    public Binding warningQueueBindingBackupExchange(@Qualifier("backupExchange") FanoutExchange backupExchange,
                                                    @Qualifier("warningQueue") Queue warningQueue) {
        return BindingBuilder.bind(warningQueue).to(backupExchange);
    }

}
