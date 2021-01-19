package com.spring.rabbitmq.xml.direct;

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
 * @date: 2020年12月29日 23:21
 * @description:
 */
public class ConsumerDirect1 {
    public static void main(String[] args) {
        //创建Spring的ApplicationContext
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:xml/spring-ribbatmq-direct.xml");
        /**
         * 获取AmqpAdmin管理类
         */
        CachingConnectionFactory connectionFactory = ctx.getBean("connectionFactory" , CachingConnectionFactory.class);
        SimpleMessageListenerContainer container = new
                SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames("direct_queue1");
        container.setMessageListener(message -> {
            System.out.println(message.toString());
        });
        container.start();
    }
}
