package com.rabbit.rabbitspringboot.delaySend;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @title：
 * @author: mengchaob
 * @date: 2021年01月11日 16:36
 * @description:
 */
@Configuration
public class DelaySendUtil {
    public final static String EXCHANGNAME="delaysendexchange";
    public final static String QUEUENAME="delaysendqueue";
    public final static String ROUNTINGKEY="delaysendroutingkey";

    @Bean
    public CachingConnectionFactory cachingConnectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setVirtualHost("factory1");
        cachingConnectionFactory.setPassword("top.mengchao.www");
        cachingConnectionFactory.setUsername("mengchaob");
        cachingConnectionFactory.setHost("127.0.0.1");
        return cachingConnectionFactory;
    }
    @Bean
    public DirectExchange directExchange(){
        DirectExchange directExchange = new DirectExchange(DelaySendUtil.EXCHANGNAME);
        directExchange.setDelayed(true);
        return directExchange;
    }

    @Bean
    public Queue queue(){
        Queue queue = new Queue(DelaySendUtil.QUEUENAME);
        return queue;
    }

    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(@Qualifier("cachingConnectionFactory") CachingConnectionFactory cachingConnectionFactory1){
        //SimpleRabbitListenerContainerFactory发现消息中有content_type有text就会默认将其转换成string类型的
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(cachingConnectionFactory1);
        return factory;
    }

}
