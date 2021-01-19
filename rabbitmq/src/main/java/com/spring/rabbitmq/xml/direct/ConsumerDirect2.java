package com.spring.rabbitmq.xml.direct;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @title：
 * @author: mengchaob
 * @date: 2020年12月29日 23:35
 * @description:
 */
public class ConsumerDirect2 {
    public static void main(String[] args) {
        //创建Spring的ApplicationContext
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:xml/spring-ribbatmq-direct-consumer.xml");
    }
}
