package com.rabbit.rabbitspringboot;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;

/**
 * @title：
 * @author: mengchaob
 * @date: 2021年01月09日 15:39
 * @description:
 */
public class ConnectionUtil {
    public static CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
    public static RabbitAdmin rabbitAdmin;
    static {
        cachingConnectionFactory.setHost("127.0.0.1");
        cachingConnectionFactory.setUsername("mengchaob");
        cachingConnectionFactory.setPassword("top.mengchao.www");
        cachingConnectionFactory.setVirtualHost("mengchaobhost");
        rabbitAdmin = new RabbitAdmin(cachingConnectionFactory);
    }
}
