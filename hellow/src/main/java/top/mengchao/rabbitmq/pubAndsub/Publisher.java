package top.mengchao.rabbitmq.pubAndsub;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import top.mengchao.rabbitmq.Common;

/**
 * 订阅发布的发布者
 */
public class Publisher {
    public static final String QUEQUENAME1="quequefanout1";
    public static final String QUEQUENAME2="quequefanout2";
    public static void main(String[] argv) throws Exception {

        try (Connection connection = Common.getConnection();
             Channel channel = connection.createChannel()) {
            /*
            1.如果没有则创建，交换机
            * 第一个参数 交换机名字
            * 第二个参数 交换机类型
            * */
            channel.exchangeDeclare(Common.QUEUE_NAME_FANOUT_EXCHANGE, "fanout");
            /**
             * 2.创建队列
             */
            channel.queueDeclare(Publisher.QUEQUENAME1, false, false, false, null);
            channel.queueDeclare(Publisher.QUEQUENAME2, false, false, false, null);
            /*3.绑定队列与交换机
            * 第一个参数，队列
            * 第二个参数，交换机
            * 第三个参数，routingkey，由于未fanout模式，所以不需要制定routingkey
            * */
            channel.queueBind(Publisher.QUEQUENAME1, Common.QUEUE_NAME_FANOUT_EXCHANGE, "");
            channel.queueBind(Publisher.QUEQUENAME2, Common.QUEUE_NAME_FANOUT_EXCHANGE, "");
            String message ="";
            for(int i=0;i<10;i++){
                message = "Publisher发送数据\"" + i ;
                /**
                 * 第一个参数，交换机名字
                 * 第二个参数，routingkey
                 */
                channel.basicPublish(Common.QUEUE_NAME_FANOUT_EXCHANGE, "",null,message.getBytes());
            }
//            channel.close();
//            connection.close();
        }
    }
}
