package indi.keter.rabbitmq.Routing;

import com.rabbitmq.client.Channel;
import indi.keter.rabbitmq.utils.RabbitMqUtils;

import java.util.Scanner;

public class DirectLogs {
    //交换机的名字
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME, "error", null, message.getBytes("utf-8"));
            System.out.println("生产者发出消息：" + message);
        }
    }
}
