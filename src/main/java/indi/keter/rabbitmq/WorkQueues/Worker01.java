package indi.keter.rabbitmq.WorkQueues;

/*
* 这是一个工作线程（消费者）
 */

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import indi.keter.rabbitmq.utils.RabbitMqUtils;

public class Worker01 {
    //队列名称
    public static final String QUEUE_NAME = "hello";

    //接收消息
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        //消息的接收
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.print("接收到的消息：" + message.getBody());
        };

        //消息的取消
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.print(consumerTag + "取消消息接收：");
        };

        System.out.println("C1等待接收消息.....");
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
