package indi.keter.rabbitmq.WorkQueues;

import com.rabbitmq.client.Channel;
import indi.keter.rabbitmq.utils.RabbitMqUtils;

import java.util.Scanner;

/*
* 生产者 发送大量消息
*/
public class Task01 {

    //队列名称
    public static final String QUEUE_NAME = "hello";

    //发送大量的消息
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        //声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        //从控制台中接收消息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            /*
             * 发送消息
             * 1.交换机
             * 2.路由的Key值是哪个（本次是队列名）
             * 3.其他参数
             * 4.发送消息的消息体
             */
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("消息发送完成：" + message);
        }

    }
}
