package top.mengchao.rabbitmq.hellow;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import top.mengchao.rabbitmq.Common;
import top.mengchao.rabbitmq.listener.Producer;

import java.util.HashMap;
import java.util.Map;

public class ConfirmConsume {
    static int i = 0;
    public static void main(String[] argv) throws Exception {
        Connection connection = Common.getConnection();
        Channel channel = connection.createChannel();
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("x-max-length",5);
        channel.exchangeDeclare(Producer.LISTENEREXCHANGE,"direct",true);
        channel.queueDeclare(Producer.QUEQULIMIT, false, false, false, args);
        channel.basicQos(1);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        /**
         * basic.ack is used for positive acknowledgements
         * basic.nack is used for negative acknowledgement
         * basic.reject is used for negative acknowledgements but has one limitation compared to basic.nack
         */
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("message:"+message);
            if (i%3==0) {
                System.out.println("basicAck:"+delivery.getEnvelope().getDeliveryTag());
                /**
                 * 第二参数，是否批量签收
                 */
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }else if(i%3==1){
                System.out.println("basicNack:"+delivery.getEnvelope().getDeliveryTag());
                /**
                 * 第二参数，是否批量签收
                 * 第三参数，是否在此放入队列
                 */
                channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false,false);
            }else {
                System.out.println("basicReject:"+delivery.getEnvelope().getDeliveryTag());
                /**
                 * 第二参数，是否批量签收
                 * 第三参数，是否在此放入队列
                 */
                channel.basicReject(delivery.getEnvelope().getDeliveryTag(), false);
            }
            i++;
        };
        /**
         * 第二个参数，是否自动签收
         */
        channel.basicConsume(Producer.QUEQULIMIT, false, deliverCallback, consumerTag -> { });
    }
}
