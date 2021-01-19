package com.rabbit.rabbitspringboot.header;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @title：
 * @author: mengchaob
 * @date: 2021年01月11日 16:36
 * @description:
 */
@Configuration
public class HeaderUtil {
    public static final String queueName="headers-queue-boot";
    public static final String headersExchangeName="headers-exchange-boot";
    public static final String routingKey="headersKey";
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
    public HeadersExchange headersExchange(CachingConnectionFactory cachingConnectionFactory){
        HeadersExchange headersExchange = new HeadersExchange(HeaderUtil.headersExchangeName);
        return headersExchange;
    }
    @Bean
    public RabbitAdmin rabbitAdmin( Queue queue,HeadersExchange headersExchange,CachingConnectionFactory cachingConnectionFactory){
        /**
         * key1=foot且key2=qqq
         * 如果map为一个空map则相当于直接按routingKey发送到队列
         */
        Map map = new HashMap();
        map.put("key1","foot");
        map.put("key2","qqqq");
        Binding binding = new Binding(HeaderUtil.queueName,Binding.DestinationType.QUEUE,HeaderUtil.headersExchangeName,HeaderUtil.routingKey,map);
        /**
         * key1=foot
         */
        Map mapKey1 = new HashMap();
        mapKey1.put("key1","foot");
        Binding bindingKey1 = new Binding(HeaderUtil.queueName,Binding.DestinationType.QUEUE,HeaderUtil.headersExchangeName,HeaderUtil.routingKey,mapKey1);
        /**
         * key1=foot
         */
        Map mapKey2 = new HashMap();
        mapKey2.put("key2","qqqq");
        Binding bindingKey2 = new Binding(HeaderUtil.queueName,Binding.DestinationType.QUEUE,HeaderUtil.headersExchangeName,HeaderUtil.routingKey,mapKey2);
        RabbitAdmin rabbitAdmin = new RabbitAdmin(cachingConnectionFactory);
        rabbitAdmin.declareExchange(headersExchange);
        rabbitAdmin.declareQueue(queue);
        rabbitAdmin.declareBinding(binding);
        rabbitAdmin.declareBinding(bindingKey1);
        rabbitAdmin.declareBinding(bindingKey2);
        return rabbitAdmin;
    }

    @Bean
    public Queue queue(){
        Queue queue = new Queue(HeaderUtil.queueName);
        return queue;
    }
}
