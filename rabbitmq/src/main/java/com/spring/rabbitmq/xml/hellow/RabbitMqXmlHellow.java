package com.spring.rabbitmq.xml.hellow;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ImportResource;

/**
 * @title：
 * @author: mengchaob
 * @date: 2020年12月21日 22:25
 * @description:
 */
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class,RabbitAutoConfiguration.class})
@ImportResource("classpath:xml/spring-ribbatmq-hellow.xml")
public class RabbitMqXmlHellow {
    private static RabbitTemplate rabbitTemplate;
    @Autowired
    @Qualifier("amqpTemplate")
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        RabbitMqXmlHellow.rabbitTemplate = rabbitTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(RabbitMqXmlHellow.class, args);
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setAppId("RabbitMqXmlHellow");
        messageProperties.setHeader("solr","ppt");
        Message message = new Message("Hello World".getBytes(),messageProperties);
        /**
         * 发送数据
         * 第一个参数routingKey，第二个参数消息体
         */
        for (int j = 0; j < 10; j++) {
            rabbitTemplate.convertAndSend("helloWorld", j+"");

        }
    }
}
