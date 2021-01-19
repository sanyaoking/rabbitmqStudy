package top.mengchao.rabbitmq.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import top.mengchao.rabbitmq.Common;

/**
 * 订阅发布的发布者
 */
public class Publisher {
    private final static String ROUTINGKEYSUB1 = "sub1_key";
    public final static String QUEUESUB1 = "queuesub1";
    private final static String ROUTINGKEYSUB2 = "sub2_key";
    public final static String QUEUESUB2 = "queuesub2";
    public static void main(String[] argv) throws Exception {

        try (Connection connection = Common.getConnection();
             Channel channel = connection.createChannel()) {
            /*
            1.如果没有则创建，交换机
            * 第一个参数 交换机名字
            * 第二个参数 交换机类型
            * */
            channel.exchangeDeclare(Common.QUEUE_NAME_DIRECT_EXCHANGE, "direct");
            /**
             * 2.创建队列
             */
            channel.queueDeclare(Publisher.QUEUESUB1, false, false, false, null);
            channel.queueDeclare(Publisher.QUEUESUB2, false, false, false, null);

            /*3.绑定队列与交换机
            * 第一个参数，队列
            * 第二个参数，交换机
            * 第三个参数，routingkey
            * */
            channel.queueBind(Publisher.QUEUESUB1, Common.QUEUE_NAME_DIRECT_EXCHANGE, Publisher.ROUTINGKEYSUB1);
            channel.queueBind(Publisher.QUEUESUB2, Common.QUEUE_NAME_DIRECT_EXCHANGE, Publisher.ROUTINGKEYSUB1);
            String message ="";
            for(int i=0;i<10;i++){
                message = "Publisher发送数据\"" + i + "\"到rountingKey:"+Publisher.ROUTINGKEYSUB1;
                /**
                 * 第一个参数，交换机名字
                 * 第二个参数，routingkey
                 */
                channel.basicPublish(Common.QUEUE_NAME_DIRECT_EXCHANGE, Publisher.ROUTINGKEYSUB1,null,message.getBytes());
            }
            for(int i=10;i<20;i++){
                message = "Publisher发送数据\"" + i + "\"到rountingKey:"+Publisher.ROUTINGKEYSUB2;
                /**
                 * 第一个参数，交换机名字
                 * 第二个参数，routingkey
                 */
                channel.basicPublish(Common.QUEUE_NAME_DIRECT_EXCHANGE, Publisher.ROUTINGKEYSUB2,null,message.getBytes());
            }
//            channel.close();
//            connection.close();
        }
    }
}
