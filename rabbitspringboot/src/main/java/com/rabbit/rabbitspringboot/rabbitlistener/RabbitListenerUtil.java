package com.rabbit.rabbitspringboot.rabbitlistener;

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
public class RabbitListenerUtil {
    public static final String queueName="listener-queue-boot";
    public static final String directExchangeName="listener-exchange-boot";
    public static final String routingKey="listenerKey";

    public static final String queueNameSecond="listener2-queue-boot";
    public static final String directExchangeNameSecond="listener2-exchange-boot";
    public static final String routingKeySecond="listener2Key";

    public static final String queueNameThird="listene3r-queue-boot";
    public static final String directExchangeNameThird="listener3-exchange-boot";
    public static final String routingKeyThird="listenerKey3";

    @Bean
    public CachingConnectionFactory cachingConnectionFactory() {
        CachingConnectionFactory ccf = new CachingConnectionFactory();
        ccf.setVirtualHost("mengchaobhost");
        ccf.setPassword("top.mengchao.www");
        ccf.setUsername("mengchaob");
        ccf.setHost("127.0.0.1");
        return ccf;
    }
}
