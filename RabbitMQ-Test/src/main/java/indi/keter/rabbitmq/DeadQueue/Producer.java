package indi.keter.rabbitmq.DeadQueue;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import indi.keter.rabbitmq.utils.RabbitMqUtils;

/*
 *  死信队列 生产者
 */
public class Producer {
    //普通交换机的名称
    public static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //延迟消息（死信消息）设置TTL时间
        //AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().expiration("10000").build();
        for (int i = 1; i <= 10; i++) {
            String message = "info" + i;
            //channel.basicPublish(NORMAL_EXCHANGE, "zhangsan", properties, message.getBytes("utf-8"));
            channel.basicPublish(NORMAL_EXCHANGE, "zhangsan", null, message.getBytes("utf-8"));
        }
    }
}
