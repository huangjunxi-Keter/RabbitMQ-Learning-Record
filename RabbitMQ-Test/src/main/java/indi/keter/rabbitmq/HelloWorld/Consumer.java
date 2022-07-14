package indi.keter.rabbitmq.HelloWorld;

/*
* 消费者 接受消息
*/

import com.rabbitmq.client.*;

public class Consumer {
    //队列名称
    public static final String QUEUE_NAME = "hello";
    //接收消息
    public static void main(String[] args) throws Exception {
        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.81.129");
        factory.setUsername("keter");
        factory.setPassword("0120");
        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        //声明 接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println(new String(message.getBody()));
        };

        //声明 取消消费
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消息消费被中断");
        };

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
