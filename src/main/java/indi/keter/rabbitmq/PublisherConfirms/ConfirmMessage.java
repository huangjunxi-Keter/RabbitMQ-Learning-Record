package indi.keter.rabbitmq.PublisherConfirms;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import indi.keter.rabbitmq.utils.RabbitMqUtils;

import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/*
 *  发布确认模式
 *  根据使用的时间 比较哪种确认方式是最好的
 *      1.单个确认模式 耗时550ms
 *      2.批量确认模式 耗时84ms
 *      3.异步批量确认 耗时45ms
 */
public class ConfirmMessage {

    //批量发消息的个数
    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {
        //1：单个确认
        //publishMessageIndividually();
        //2：批量确认
        //publishMessageBatch();
        //3：异步批量确认
        publishMessageAsync();
    }

    //单个确认
    public static void publishMessageIndividually() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //对列名称
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);

        //开启发布确认
        channel.confirmSelect();

        //开始时间
        long begin = System.currentTimeMillis();
        
        //批量发消息
        for (int i = 1; i <= MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes("utf-8"));
            //单个消息就马上进行发布确认
            boolean flag = channel.waitForConfirms();
        }

        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个单独确认消息，耗时" + (end - begin) + "ms");
    }

    //批量确认
    public static void publishMessageBatch() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //对列名称
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);

        //开启发布确认
        channel.confirmSelect();

        //批量确认消息大小
        int batchSize = 100;

        //开始时间
        long begin = System.currentTimeMillis();

        //批量发消息 && 批量发布确认
        for (int i = 1; i <= MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes("utf-8"));

            //判断达到100条消息的时候 批量确认一次
            if (i % 100 == 0){
                //发布确认
                channel.waitForConfirms();
            }
        }

        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个批量确认消息，耗时" + (end - begin) + "ms");
    }

    //异步批量确认
    public static void publishMessageAsync() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //对列名称
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);

        //开启发布确认
        channel.confirmSelect();

        /*
         *  线程安全有序的一个哈希表 适用于高并发的情况下
         *  1.轻松的将序号与消息进行管理
         *  2.可以轻松的批量删除条目 只要提供序号
         *  3.支持高并发（多线程）
         */
        ConcurrentSkipListMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();

        //消息确认成功 回调函数
        /*
         *  1.消息的标识
         *  2.是否为批量确认
         */
        ConfirmCallback ackCallback = (deliveryTag, mulitiple) -> {
            if (mulitiple) {
                //2：删除掉已经确认的消息 剩下的就是未确认的消息
                ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(deliveryTag);
                confirmed.clear();
            } else {
                outstandingConfirms.remove(deliveryTag);
            }
            System.out.println("确认的消息：" + deliveryTag);
        };
        //消息确认失败 回调函数
        /*
         *  1.消息的标识
         *  2.是否为批量确认
         */
        ConfirmCallback nackCallback = (deliveryTag, mulitiple) -> {
            System.out.println("未确认的消息：" + deliveryTag);
            //3：打印一下未确认的消息都有哪些
        };
        //消息监听器 监听哪些消息成功 哪些消息失败
        /*
         *  1.监听哪些消息成功了
         *  2.监听哪些消息失败了
         */
        channel.addConfirmListener(ackCallback, nackCallback);//异步

        //开始时间
        long begin = System.currentTimeMillis();

        //批量发消息
        for (int i = 1; i <= MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes("utf-8"));
            //1：此处记录下所有要发送的消息 消息总和
            outstandingConfirms.put(channel.getNextPublishSeqNo(), message);
        }

        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个异步批量确认消息，耗时" + (end - begin) + "ms");
    }

}
