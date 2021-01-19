package com.spring.rabbitmq.xml.pubsub;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryContext;

import java.util.List;

/**
 * @title：
 * @author: mengchaob
 * @date: 2020年12月25日 14:12
 * @description: java 配置客户端监听，广播发布队列queue1消费者
 */
public class Queue1Consuer {
    public static void main(String[] args) throws Exception {
        //创建Spring的ApplicationContext
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:xml/spring-ribbatmq-pubsub.xml");
        RabbitTemplate rabbitTemplate = ctx.getBean("amqpTemplate" , RabbitTemplate.class);
        /**
         * 获取AmqpAdmin管理类
         */
        AmqpAdmin amqpAdmin = ctx.getBean("admin" , AmqpAdmin.class);
        /**
         * 创建队列queue
         */
        Queue queue1 = new Queue("queue1");
        amqpAdmin.declareQueue(queue1);
        CachingConnectionFactory connectionFactory = ctx.getBean("connectionFactory" , CachingConnectionFactory.class);
        SimpleMessageListenerContainer container = new
                SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames("queue1");
        container.setMessageListener(new MessageListenerImpl());
        container.start();
    }
    static class MessageListenerImpl implements MessageListener{
        @Override
        public void onMessage(Message message) {
            System.out.println("监听生效了，收到的消息:"+message);
        }

        @Override
        public void containerAckMode(AcknowledgeMode mode) {
            System.out.println("监听生效了，containerAckMode 收到的信息:"+mode);
        }

        @Override
        public void onMessageBatch(List<Message> messages) {
            System.out.println("监听生效了，onMessageBatch 收到的消息:"+messages);
        }
    }
    static class RecoveryCallbackImpl implements RecoveryCallback  {
        @Override
        public Object recover(RetryContext retryContext) throws Exception {
            System.out.println("consumer1 接收到消息内容：");
            return null;
        }
    }
    static class MessagePostProcessorImpe implements MessagePostProcessor{
        @Override
        public Message postProcessMessage(Message message) throws AmqpException {
            System.out.println(" postProcessMessage(Message message) 接收到消息内容"+message);
            return message;
        }

        @Override
        public Message postProcessMessage(Message message, Correlation correlation) {
            System.out.println(" postProcessMessage(Message message, Correlation correlation) 接收到消息内容"+message);
            return message;
        }
    }
}
