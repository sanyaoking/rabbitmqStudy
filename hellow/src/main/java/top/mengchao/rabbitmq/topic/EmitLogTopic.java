package top.mengchao.rabbitmq.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import top.mengchao.rabbitmq.Common;
import top.mengchao.rabbitmq.pubAndsub.Publisher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @title：
 * @author: mengchaob
 * @date: 2020年12月17日 14:12
 * @description:
 */
public class EmitLogTopic {
    public static final String QUEUETOPIC1 = "queuetopic1";
    public static final String QUEUETOPIC2 = "queuetopic2";
    public static final String QUEUETOPIC3 = "queuetopic3";
    public static final String QUEUETOPIC4 = "queuetopic4";
    public static final String ROUTINGKEY1 = "mengchao.*";
    public static final String ROUTINGKEY2 = "mengchao.#";
    public static final String ROUTINGKEY3 = "#.mengchao";
    public static final String ROUTINGKEY4 = "*.mengchao";
    public static final List<String> routingKeyList = new ArrayList();
    static {
        routingKeyList.add("mengchao.hebei.2");
        routingKeyList.add("mengchao.nanjing");
        routingKeyList.add("shanghai.mengchao");
        routingKeyList.add("1.beijing.mengchao");
    }
    public static void main(String[] argv) throws Exception {
        try (Connection connection = Common.getConnection();
             Channel channel = connection.createChannel()) {
            /**
             * 创建交换机，没有则创建，有责不做处理
             */
            channel.exchangeDeclare(Common.QUEUE_NAME_TOPIC_EXCHANGE, "topic");
            /**
             * 创建队列
             */
            channel.queueDeclare(EmitLogTopic.QUEUETOPIC1, false, false, false, null);
            channel.queueDeclare(EmitLogTopic.QUEUETOPIC2, false, false, false, null);
            channel.queueDeclare(EmitLogTopic.QUEUETOPIC3, false, false, false, null);
            channel.queueDeclare(EmitLogTopic.QUEUETOPIC4, false, false, false, null);
            /**
             * 1.绑定队列和交换机
             * 2.由于绑定的是*和#的routingkey值，所以如果发送数据的时候选用的routingkey的值能够匹配上待*或者#的routingkey,则会发送的对应的队列，
             * 如果符合两个，那么就会发送到两个绑定的队列中去。
             * 3. 此处的绑定也可以在web应用或者消费端设置
             */
            channel.queueBind(EmitLogTopic.QUEUETOPIC1,Common.QUEUE_NAME_TOPIC_EXCHANGE,EmitLogTopic.ROUTINGKEY1);
            channel.queueBind(EmitLogTopic.QUEUETOPIC2,Common.QUEUE_NAME_TOPIC_EXCHANGE,EmitLogTopic.ROUTINGKEY2);
            channel.queueBind(EmitLogTopic.QUEUETOPIC3,Common.QUEUE_NAME_TOPIC_EXCHANGE,EmitLogTopic.ROUTINGKEY3);
            channel.queueBind(EmitLogTopic.QUEUETOPIC4,Common.QUEUE_NAME_TOPIC_EXCHANGE,EmitLogTopic.ROUTINGKEY4);
            EmitLogTopic.routingKeyList.forEach(e->{
                for (int i = 0; i < 8; i++) {
                    String message = "exchange:" + Common.QUEUE_NAME_TOPIC_EXCHANGE + ";info:{" + i+e+"}";
                    System.out.println(" [x] Sent '" + Common.QUEUE_NAME_TOPIC_EXCHANGE + "':'" + message + "'");
                    /**
                     * 第一个参数，交换机名字
                     * 第二个参数，routingkey
                     */
                    try {
                        channel.basicPublish(Common.QUEUE_NAME_TOPIC_EXCHANGE, e, null, message.getBytes("UTF-8"));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });
//            channel.close();
//            connection.close();
        }
    }
}
