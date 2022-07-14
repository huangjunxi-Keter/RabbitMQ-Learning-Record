package indi.keter.rabbitmq.PublishSubscribe;

/*
 *  发消息 交换机
 */

import com.rabbitmq.client.Channel;
import indi.keter.rabbitmq.utils.RabbitMqUtils;

import java.util.Scanner;

public class EmitLog {
    //交换机的名字
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("utf-8"));
            System.out.println("生产者发出消息：" + message);
        }
    }
}
