package com.spring.rabbitmq.xml.listener;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @title：
 * @author: mengchaob
 * @date: 2020年12月30日 20:02
 * @description:
 */
public class ConsumerConfirm {
    public static int flag = 0;
    public static void main(String[] args) {
        //创建Spring的ApplicationContext
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:xml/spring-ribbatmq-confirm.xml");
        /**
         * 获取AmqpAdmin管理类
         */
        CachingConnectionFactory connectionFactory = ctx.getBean("connectionFactory" , CachingConnectionFactory.class);
        SimpleMessageListenerContainer container = new
                SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames("direct_queue2");

        // 设置监听消息的方法，匿名内部类方式
        container.setMessageListener((ChannelAwareMessageListener) (message, channel) -> {
            // 手动确认
            long deliveryTag = message.getMessageProperties().getDeliveryTag();

            if(flag/3==0){

                /**
                 * 第一个为标识，第二个为是否在放回队列,被放回队列的消息还有可能会被这个客户端再次消费
                 */
                channel.basicReject(deliveryTag, false); // 拒绝
            }else if(flag/3==1) {
                /**
                 * 第一个为标识，第二位是否拒绝多个，第三个为拒绝后是否在放入队列，被放回队列的消息还有可能会被这个客户端再次消费
                 * deliveryTag:发布的每一条消息都会获得一个唯一的deliveryTag，它在channel范围内是唯一的
                 *
                 * multiple：批量确认标志，为true表示包含当前消息在内的所有比该消息的deliveryTag值小的消息都被拒绝， 除了已经被应答的消息。为false则表示只拒绝本条消息
                 *
                 * requeue：表示如何处理这条消息，为true表示重新放入RabbitMQ的发送队列中，为false表示通知RabbitMQ销毁该消息
                 */
                channel.basicNack(deliveryTag, false,true); // 拒绝
            }else{
                /**
                 * 第一个为标识，第二个为是否签收多个
                 */
                System.out.println("签收的消息："+message.toString());
                channel.basicAck(deliveryTag,false);
            }
            flag++;
        });
        /**
         * 设置为手动签收
         */
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        container.start();
    }
}
