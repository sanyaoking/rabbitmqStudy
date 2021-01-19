package com.rabbit.rabbitspringboot.direct;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.SimpleRoutingConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.HashMap;
import java.util.Map;

/**
 * @title：
 * @author: mengchaob
 * @date: 2021年01月11日 16:36
 * @description:
 */
@Configuration
public class DirectUtil {
    public static final String queueName="direct-queue-boot";
    public static final String directExchangeName="direct-exchange-boot";
    public static final String routingKey="directKey";
    @Bean
    public CachingConnectionFactory cachingConnectionFactory() {
        CachingConnectionFactory ccf = new CachingConnectionFactory();
        ccf.setVirtualHost("mengchaobhost");
        ccf.setPassword("top.mengchao.www");
        ccf.setUsername("mengchaob");
        ccf.setHost("127.0.0.1");
        return ccf;
    }

    @Bean
    public DirectExchange directExchange(CachingConnectionFactory cachingConnectionFactory){
        DirectExchange directExchange = new DirectExchange(DirectUtil.directExchangeName,true,false);
        return directExchange;
    }

    @Bean
    public Queue queue(){
        Queue queue = new Queue(DirectUtil.queueName);
        return queue;
    }
}
