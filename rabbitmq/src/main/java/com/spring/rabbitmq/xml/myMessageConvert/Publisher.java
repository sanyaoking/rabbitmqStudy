package com.spring.rabbitmq.xml.myMessageConvert;

import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
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
 * @date: 2021年01月04日 10:11
 * @description:  消费者处理信息由于各种可能原因，停止本条消息处理，可以选择将消息返回给队列或者丢弃，本例是将消息返回队列
 */
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class,RabbitAutoConfiguration.class})
@ImportResource("classpath:xml/spring-ribbatmq-returnmessage.xml")
public class Publisher {
    private static RabbitTemplate rabbitTemplate;
    private static ConnectionFactory connectionFactory;
    @Autowired
    @Qualifier("connectionFactory")
    public void setConnectionFactory(ConnectionFactory connectionFactory) {
        Publisher.connectionFactory = connectionFactory;
    }
    @Autowired
    @Qualifier("amqpTemplate")
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        com.spring.rabbitmq.xml.myMessageConvert.Publisher.rabbitTemplate = rabbitTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(com.spring.rabbitmq.xml.myMessageConvert.Publisher.class, args);
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setAppId("pubsub");
        messageProperties.setHeader("solr","ppt");
        messageProperties.setCorrelationId("com.spring.rabbitmq.xml.myMessageConvert.Publisher");
        rabbitTemplate.setMessageConverter(new MessageConverterImpl());
        /**
         * 发送数据
         * 第一个参数routingKey，第二个参数消息体
         */
        for (int j = 0; j < 10; j++) {
            MyMessage message = new MyMessage((j+""),messageProperties);
            rabbitTemplate.convertAndSend( "message.key1",message);
        }
    }
}
