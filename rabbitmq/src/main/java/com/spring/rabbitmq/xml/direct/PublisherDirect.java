package com.spring.rabbitmq.xml.direct;

import com.spring.rabbitmq.xml.topic.PublisherTopic;
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
 * @date: 2020年12月29日 23:19
 * @description:
 */
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class,RabbitAutoConfiguration.class})
@ImportResource("classpath:xml/spring-ribbatmq-direct.xml")
public class PublisherDirect {
    private static RabbitTemplate rabbitTemplate;
    @Autowired
    @Qualifier("amqpTemplate")
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        PublisherDirect.rabbitTemplate = rabbitTemplate;
    }
    public static void main(String[] args) {
        SpringApplication.run(PublisherDirect.class, args);
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setAppId("publishDirect");
        messageProperties.setHeader("className","PublisherDirect");
        /**
         * 发送数据
         * 第一个参数routingKey，第二个参数消息体
         */
        for (int j = 0; j < 10; j++) {
            Message message = new Message(("direct.key1 send info:"+j+"").getBytes(),messageProperties);
            PublisherDirect.rabbitTemplate.convertAndSend( "direct.key1",message);
        }
        for (int j = 0; j < 10; j++) {
            Message message = new Message(("direct.key2 send info:"+j+"").getBytes(),messageProperties);
            PublisherDirect.rabbitTemplate.convertAndSend( "direct.key2",message);
        }
    }
}
