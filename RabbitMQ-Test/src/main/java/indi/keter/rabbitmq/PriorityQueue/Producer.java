package indi.keter.rabbitmq.PriorityQueue;

/*
*生产者 ： 发消息（优先级队列）
 */

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;

public class Producer {
    //消息队列
    public static final String QUEUE_NAME = "hello";

    //发消息
    public static void main(String[] args) throws Exception {
        //创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //工程IP 连接RabbitMQ的队列
        factory.setHost("192.168.81.129");
        //用户名&密码
        factory.setUsername("keter");
        factory.setPassword("0120");
        //创建连接
        Connection connection = factory.newConnection();
        //获取信道
        Channel channel = connection.createChannel();
        /*
        *生成一个队列
        * 1.队列名称
        * 2.是否保存消息（队列里面的消息是否持久化【默认情况下消息存储在内存中，持久化存储在磁盘上】）
        * 3.该队列是否共享
        * 4.是否自动删除（最后一个消费者断开连接之后是否删除队列）
        * 5.其他参数
        */
        Map<String, Object> arguments = new HashMap<>(1);
        arguments.put("x-max-priority", 10); //官方允许是0-255之间 此处设置10 允许优化级范围为0-10
        channel.queueDeclare(QUEUE_NAME, true, false, true, arguments);
        //发消息
        for (int i = 1; i <= 10; i++) {
            String message = "info" + i;
            AMQP.BasicProperties properties = null;
            if (i == 5) {
                properties = new AMQP.BasicProperties().builder().priority(5).build();
            }
            /*
             *生成一个队列
             * 1.交换机
             * 2.路由的Key值是哪个（本次是队列名）
             * 3.其他参数
             * 4.发送消息的消息体
             */
            channel.basicPublish("", QUEUE_NAME, properties, message.getBytes());
        }

        System.out.println("消息发送完毕");
    }

}
