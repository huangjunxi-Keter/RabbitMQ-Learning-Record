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
        System.out.println("C2_等待接收消息处理 时间较长");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            //沉睡一秒钟
            SleepUtils.sleep(30);
            System.out.println("接收到的消息：" + new String(message.getBody(), "utf-8"));
            /*
             *  手动应答
             *  1.消息的标记 Tag
             *  2.是否批量应答 false不批量 true批量
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };

        //设置不公平分发
        //RabbitMQ中的概念，channel.basicQos(1)指该消费者在接收到队列里的消息但没有返回确认结果之前,队列不会将新的消息分发给该消费者。队列中没有被消费的消息不会被删除，还是存在于队列中。
        //int prefetchCount = 1;
        //预取值 在信道中能存在的消息数量（已被处理完成的消息不占用信道）
        int prefetchCount = 5;
        channel.basicQos(prefetchCount);

        //手动应答
        boolean autoAck = false;
        channel.basicConsume(TASK_QUEUE_NAME, autoAck, deliverCallback, (consumerTag) -> {});
    }
}
