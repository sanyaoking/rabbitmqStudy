package com.rabbit.rabbitspringboot.delay1;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
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
    public static String exchangeName ="ex-dead-boot";
    public static String routing_key="dead-key";
    public static String queueName="timeoutdead-queue-boot";
    public final static String queueDeadName="dead-queue-boot";
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
    public DirectExchange directExchange(@Autowired RabbitAdmin rabbitAdmin){
        /**
         * 第一个参数 交换机名称
         * 第二个参数 是否持久化
         * 第三个参数 是否自动删除
         */
        DirectExchange directExchange= new DirectExchange(DelayConfig.exchangeName,true,false);
        rabbitAdmin.declareExchange(directExchange);
        return directExchange;
    }
    @Bean
    public Queue queue(@Autowired RabbitAdmin rabbitAdmin,@Autowired DirectExchange directExchange){
        /**
         * 队里设置超时时间
         */
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-message-ttl",10000);
        /**
         * 设置系统超时后，队列内信息的去向
         */
        arguments.put("x-dead-letter-exchange",DelayConfig.exchangeName);
        arguments.put("x-dead-letter-routing-key",DelayConfig.routing_key);
        /**
         * 绑定交换机和路由key
         */
        Queue deadQueue = new Queue(DelayConfig.queueDeadName);
        rabbitAdmin.declareQueue(deadQueue);
        Binding binding = new Binding(DelayConfig.queueDeadName, Binding.DestinationType.QUEUE,DelayConfig.exchangeName,DelayConfig.routing_key,null);//BindingBuilder.bind(deadQueue).to(directExchange).with(DelayConfig.routing_key);
        rabbitAdmin.declareBinding(binding);
        /**
         * 第三个参数，是否为本链接专用
         */
        Queue queue = new Queue(DelayConfig.queueName,true,false,true,arguments);
        rabbitAdmin.declareQueue(queue);
        return queue;
    }
    @Bean
    public RabbitTemplate rabbitTemplate(DirectExchange directExchange ,CachingConnectionFactory cachingConnectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        return rabbitTemplate;
    }
    @Bean
    public RabbitAdmin rabbitAdmin(CachingConnectionFactory cachingConnectionFactory){
        RabbitAdmin rabbitAdmin = new RabbitAdmin(cachingConnectionFactory);
        return rabbitAdmin;
    }
}
