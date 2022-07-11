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
            System.out.print("接收到的消息：" + new String(message.getBody()));
        };

        //消息的取消
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.print(consumerTag + "取消消息接收：");
        };

        System.out.println("C2等待接收消息.....");

        /*
         * 消费者 接收消息
         * 1.消费哪个队列
         * 2.消费成功之后是否自动应答 true 自动应答 false 手动应答
         * 3.接收成功的回调
         * 4.取消消费的回调
         */
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
