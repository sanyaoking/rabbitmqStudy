package com.rabbit.rabbitspringboot.selfMessage;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @title：
 * @author: mengchaob
 * @date: 2021年01月11日 16:36
 * @description:
 */
@Configuration
public class MessageUtil {
    public static final String queueName="message-queue-boot";
    public static final String messageExchangeName="message-exchange-boot";
    public static final String routingKey="messageKey";
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
        DirectExchange directExchange = new DirectExchange(MessageUtil.messageExchangeName,true,false);
        return directExchange;
    }

    @Bean
    public Queue queue(){
        Queue queue = new Queue(MessageUtil.queueName);
        return queue;
    }
}
