package com.rabbit.rabbitspringboot.timeout;


import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @title：
 * @author: mengchaob
 * @date: 2021年01月09日 14:46
 * @description:
 */
@Configuration
public class DelayConfig {
    public static String routing_key="timeoutqueue-key";
    public static String queueName="timeoutqueue-queue-boot";
    @Bean
    public CachingConnectionFactory cachingConnectionFactory(){
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setHost("127.0.0.1");
        cachingConnectionFactory.setUsername("mengchaob");
        cachingConnectionFactory.setPassword("top.mengchao.www");
        cachingConnectionFactory.setVirtualHost("mengchaobhost");
        return cachingConnectionFactory;
    }

    @Bean
    public DirectExchange directExchange(){
        /**
         * 第一个参数 交换机名称
         * 第二个参数 是否持久化
         * 第三个参数 是否自动删除
         */
        DirectExchange directExchange= new DirectExchange("ex-timeoutqueue-boot",true,false);
        return directExchange;
    }
    @Bean
    public Queue queue(){
        /**
         * 队里设置超时时间
         */
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-message-ttl",10000);

        Queue queue = new Queue(DelayConfig.queueName,true,false,true,arguments);
        return queue;
    }
    @Bean
    public RabbitTemplate rabbitTemplate(DirectExchange directExchange ,CachingConnectionFactory cachingConnectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        /*rabbitTemplate.setRoutingKey("timeoutqueue-key");
        rabbitTemplate.setExchange(directExchange.getName());*/
        return rabbitTemplate;
    }
    @Bean
    public RabbitAdmin rabbitAdmin(CachingConnectionFactory cachingConnectionFactory){
        RabbitAdmin rabbitAdmin = new RabbitAdmin(cachingConnectionFactory);
        return rabbitAdmin;
    }
}
