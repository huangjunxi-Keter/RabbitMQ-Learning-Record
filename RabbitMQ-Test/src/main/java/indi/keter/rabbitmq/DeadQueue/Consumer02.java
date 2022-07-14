package indi.keter.rabbitmq.DeadQueue;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import indi.keter.rabbitmq.utils.RabbitMqUtils;

import java.util.HashMap;
import java.util.Map;

/*
 *  死信队列 实战
 *  消费者2
 */
public class Consumer02 {
    //死信队列的名称
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        System.out.println("等待接收消息......");

        //接收消息回调
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("Consumer02接收的消息是：" + new String(message.getBody(), "utf-8"));
        };

        channel.basicConsume(DEAD_QUEUE, true, deliverCallback, consumerTag -> {});
    }
}
