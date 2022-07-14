package indi.keter.rabbitmq.MessageManualAnswer;

/*
 *  消息在手动应答时不丢失 放回队列重新消费
 */

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import indi.keter.rabbitmq.utils.RabbitMqUtils;

import java.util.Scanner;

public class Task02 {
    //队列名称
    public static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //开启发布确认
        channel.confirmSelect();
        //需要让Queue进行持久化（防止MQ掉线造成的数据丢失）
        boolean durable = true;
        //声明队列
        channel.queueDeclare(TASK_QUEUE_NAME, durable, false, false, null);
        //从控制台中输入信息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            //设置生产者发送消息为持久化（将消息保存到硬盘）MessageProperties.PERSISTENT_TEXT_PLAIN
            channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("utf-8"));
            System.out.println("生产者发出消息：" + message);
        }
    }
}
