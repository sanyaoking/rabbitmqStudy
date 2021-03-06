package com.spring.rabbitmq.xml.topic;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @title：
 * @author: mengchaob
 * @date: 2020年12月29日 18:14
 * @description:
 */
public class ConsumerTopic2 {
    public static void main(String[] args) {
        //创建Spring的ApplicationContext
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:xml/spring-ribbatmq-topic.xml");
        RabbitTemplate rabbitTemplate = ctx.getBean("amqpTemplate" , RabbitTemplate.class);
        /**
         * 获取AmqpAdmin管理类
         */
        AmqpAdmin amqpAdmin = ctx.getBean("admin" , AmqpAdmin.class);
        Queue queue1 = new Queue("topic_queue2");
        amqpAdmin.declareQueue(queue1);
        CachingConnectionFactory connectionFactory = ctx.getBean("connectionFactory" , CachingConnectionFactory.class);
        SimpleMessageListenerContainer container = new
                SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames("topic_queue2");
        container.setMessageListener(message -> {
            System.out.println(message.toString());
        });
        container.start();
    }
}
