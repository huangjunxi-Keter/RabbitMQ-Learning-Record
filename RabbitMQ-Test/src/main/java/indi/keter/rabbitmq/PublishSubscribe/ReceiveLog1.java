package indi.keter.rabbitmq.PublishSubscribe;

/*
 *  消息的接收
 */

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import indi.keter.rabbitmq.utils.RabbitMqUtils;

public class ReceiveLog1 {
    //交换机的名字
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        //声明一个队列 临时
        /*
         *  生成一个临时队列，队列的名称是随机的
         *  当消费者断开与队列的连接，队列自动删除
         */
        String queue = channel.queueDeclare().getQueue();
        /*
         *  绑定交换机与队列
         */
        channel.queueBind(queue, EXCHANGE_NAME, "");
        System.out.println("等待接收消息，把消息打印在屏幕上......");

        //接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("ReceiveLog1控制台打印接收到的消息：" + new String(message.getBody(), "utf-8"));
        };

        channel.basicConsume(queue, true, deliverCallback, consumerTag -> {});
    }
}
