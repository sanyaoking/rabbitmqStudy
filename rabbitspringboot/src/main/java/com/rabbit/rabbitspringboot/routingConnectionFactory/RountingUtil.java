package com.rabbit.rabbitspringboot.routingConnectionFactory;

import org.springframework.amqp.core.DirectExchange;
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
import org.springframework.amqp.core.Queue;

/**
 * @title：
 * @author: mengchaob
 * @date: 2021年01月11日 16:36
 * @description:
 */
@Configuration
public class RountingUtil {

    @Bean(name = "factory1")
    public CachingConnectionFactory getFactory1() {
        CachingConnectionFactory ccf = new CachingConnectionFactory();
        ccf.setVirtualHost("factory1");
        ccf.setPassword("top.mengchao.www");
        ccf.setUsername("mengchaob");
        ccf.setHost("127.0.0.1");
        return ccf;
    }
    @Bean(name = "factory2")
    public CachingConnectionFactory getFactory2() {
        CachingConnectionFactory ccf = new CachingConnectionFactory();
        ccf.setVirtualHost("factory2");
        ccf.setPassword("top.mengchao.www");
        ccf.setUsername("mengchaob");
        ccf.setHost("127.0.0.1");
        return ccf;
    }

    /**
     *
     * @param cachingConnectionFactory1
     * @param cachingConnectionFactory2
     * @return
     * 必须加上@Primary注解否则会报错，由于SimpleRoutingConnectionFactory是我们最终需要的bean所以我们在此加上Primary注解
     * .RabbitAnnotationDrivenConfiguration required a single bean, but 3 were found:
     */
    @Primary
    @Bean(name = "simpleRoutingConnectionFactory")
    public SimpleRoutingConnectionFactory simpleRoutingConnectionFactory(@Qualifier("factory1") CachingConnectionFactory cachingConnectionFactory1, @Qualifier("factory2") CachingConnectionFactory cachingConnectionFactory2){
        SimpleRoutingConnectionFactory simpleRoutingConnectionFactory = new SimpleRoutingConnectionFactory();
        Map<Object, ConnectionFactory> targetConnectionFactories = new HashMap<>();
        targetConnectionFactories.put("factory1",cachingConnectionFactory1);
        targetConnectionFactories.put("factory2",cachingConnectionFactory2);
        simpleRoutingConnectionFactory.setTargetConnectionFactories(targetConnectionFactories);
        return simpleRoutingConnectionFactory;
    }

    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory1(@Qualifier("factory1") CachingConnectionFactory cachingConnectionFactory1){
        //SimpleRabbitListenerContainerFactory发现消息中有content_type有text就会默认将其转换成string类型的
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(cachingConnectionFactory1);
        SimpleMessageListenerContainer SimpleMessageListenerContainer =  factory.createListenerContainer();
        return factory;
    }

    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory2(@Qualifier("factory2") CachingConnectionFactory cachingConnectionFactory1){
        //SimpleRabbitListenerContainerFactory发现消息中有content_type有text就会默认将其转换成string类型的
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(cachingConnectionFactory1);
        return factory;
    }

}
