package top.mengchao.rabbitmq.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import top.mengchao.rabbitmq.Common;
import top.mengchao.rabbitmq.pubAndsub.Publisher;

/**
 * @title：
 * @author: mengchaob
 * @date: 2020年12月17日 16:45
 * @description:
 */
public class ReceiveLogsTopic2 {

    public static void main(String[] argv) throws Exception {
        Connection connection = Common.getConnection();
        Channel channel = connection.createChannel();
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };
        channel.basicConsume(EmitLogTopic.QUEUETOPIC2, true, deliverCallback, consumerTag -> { });
    }
}
