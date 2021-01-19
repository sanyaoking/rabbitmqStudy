package top.mengchao.rabbitmq.pubAndsub;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import top.mengchao.rabbitmq.Common;

public class Subscriber2 {
    private static void doWork(String task) throws InterruptedException {
        Thread.sleep(1000);
    }
    public static void main(String[] argv) throws Exception {
        Connection connection = Common.getConnection();
        Channel channel = connection.createChannel();
        /**
         * 绑定队列
         */
        System.out.println(" Subscriber2 is running!");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" Subscriber2 Received '" + message + "'");
            try {
                doWork(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println(" Subscriber2 Done");
            }

        };
        /**
         * 第一个参数，routingkey
         * 第二个参数，是否自动签收
         */
        channel.basicConsume(Publisher.QUEQUENAME2, true, deliverCallback, consumerTag -> { });

    }
}
