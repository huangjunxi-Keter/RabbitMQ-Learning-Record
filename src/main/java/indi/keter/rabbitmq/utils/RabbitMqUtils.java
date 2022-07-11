package indi.keter.rabbitmq.utils;

import com.rabbitmq.client.*;

/*
* 此类为连接工厂、创建信道的工具类
*/
public class RabbitMqUtils {
    //得到一个连接的 channel
    public static Channel getChannel() throws Exception {
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
        return channel;
    }
}