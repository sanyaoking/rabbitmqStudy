package com.spring.rabbitmq.xml.myMessageConvert;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @title：
 * @author: mengchaob
 * @date: 2021年01月04日 14:22
 * @description: 通过container来实现持续监听队列，并返回reply
 */
public class ReturnMessageContain {
    public static void main(String[] args) {
        //创建Spring的ApplicationContext
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:xml/spring-ribbatmq-consumer-returnmessage.xml");
        Queue returnmessage_queue1 = ctx.getBean("returnmessage_queue1" , Queue.class);
        /**
         * 消费
         */
        CachingConnectionFactory connectionFactory = ctx.getBean("connectionFactory1" , CachingConnectionFactory.class);
        RabbitTemplate amqpTemplate = ctx.getBean("amqpTemplate" , RabbitTemplate.class);
        amqpTemplate.setMessageConverter(new MessageConverterImpl());
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueues(returnmessage_queue1);
        container.setMessageListener(new ChannelAwareMessageListenerImpl());
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        container.start();

    }
}

class ChannelAwareMessageListenerImpl implements ChannelAwareMessageListener{
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        String index = new String(message.getBody());
        int flag = Integer.parseInt(index);
        if(flag%3==0){
            /**
             * 第一个为标识，第二个为是否在放回队列,被放回队列的消息还有可能会被这个客户端再次消费
             */
            channel.basicReject(deliveryTag, false); // 拒绝
        }else if(flag%3==1) {
            /**
             * 第一个为标识，第二位是否拒绝多个，第三个为拒绝后是否在放入队列，被放回队列的消息还有可能会被这个客户端再次消费
             * deliveryTag:发布的每一条消息都会获得一个唯一的deliveryTag，它在channel范围内是唯一的
             *
             * multiple：批量确认标志，为true表示包含当前消息在内的所有比该消息的deliveryTag值小的消息都被拒绝， 除了已经被应答的消息。为false则表示只拒绝本条消息
             *
             * requeue：表示如何处理这条消息，为true表示重新放入RabbitMQ的发送队列中，为false表示通知RabbitMQ销毁该消息
             */
//            channel.basicNack(deliveryTag, false,true); // 拒绝
        }else{
            /**
             * 第一个为标识，第二个为是否签收多个
             */
            System.out.println("签收的消息："+message.toString());
            channel.basicAck(deliveryTag,false);
        }
    }
}
