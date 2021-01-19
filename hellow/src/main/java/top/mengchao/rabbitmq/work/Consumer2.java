package top.mengchao.rabbitmq.work;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import top.mengchao.rabbitmq.Common;

public class Consumer2 {
    private static void doWork(String task) throws InterruptedException {
            Thread.sleep(2000);
    }
    public static void main(String[] argv) throws Exception {
        Connection connection = Common.getConnection();
        final Channel channel = connection.createChannel();
        channel.queueDeclare(Common.QUEUE_NAME_WORK, false, false, false, null);
        //如果不写basicQos（1），则自动MQ会将所有请求平均发送给所有消费者
        //basicQos,MQ不再对消费者一次发送多个请求，而是消费者处理完一个消息后（确认后），在从队列中获取一个新的
        channel.basicQos(1);//处理完一个取一个
        System.out.println(" Consumer2 is running!");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" Consumer2 Received '" + message + "'");
            try {
                doWork(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println(" Consumer2 Done");
                //手动签收
                channel.basicAck(delivery.getEnvelope().getDeliveryTag() , false);
            }
        };
        /**
         * true 自动签收
         * false 手动签收
         */
        boolean autoAck = false;
        channel.basicConsume(Common.QUEUE_NAME_WORK, autoAck, deliverCallback, consumerTag -> {
            System.out.println(consumerTag);
        });

    }
}
