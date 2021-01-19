package top.mengchao.rabbitmq.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import top.mengchao.rabbitmq.Common;

public class Subscriber1 {
    private final static String ROUTINGKEY = "sub1_key";
    private static void doWork(String task) throws InterruptedException {
        Thread.sleep(1000);
    }
    public static void main(String[] argv) throws Exception {
        Connection connection = Common.getConnection();
        Channel channel = connection.createChannel();
        /**
         * 绑定队列
         */
        System.out.println(" Subscriber1 is running!");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" Subscriber1 routingkey:"+Subscriber1.ROUTINGKEY+ "Received '" + message + "'");
            try {
                doWork(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println(" Subscriber1 Done");
            }

        };
        /**
         * 第一个参数，routingkey
         * 第二个参数，是否自动签收
         */
        channel.basicConsume(Publisher.QUEUESUB1, true, deliverCallback, consumerTag -> { });

    }
}
