package indi.keter.rabbitmq.MessageManualAnswer;

/*
 *  消息在手动应答时不丢失，放回队列重新消费
 */

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import indi.keter.rabbitmq.utils.RabbitMqUtils;
import indi.keter.rabbitmq.utils.SleepUtils;

public class Worker03 {
    //队列名称
    public static final String TASK_QUEUE_NAME = "ack_queue";

    //接收消息
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("C1_等待接收消息处理 时间较短");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            //沉睡一秒钟
            SleepUtils.sleep(1);
            System.out.println("接收到的消息：" + new String(message.getBody(), "utf-8"));
            /*
             *  手动应答
             *  1.消息的标记 Tag
             *  2.是否批量应答 false不批量 true批量
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };

        //手动应答
        boolean autoAck = false;
        channel.basicConsume(TASK_QUEUE_NAME, autoAck, deliverCallback, (consumerTag) -> {});
    }
}
