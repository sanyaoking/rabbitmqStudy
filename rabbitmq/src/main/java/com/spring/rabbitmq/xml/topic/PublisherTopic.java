package com.spring.rabbitmq.xml.topic;

import com.spring.rabbitmq.xml.pubsub.Publisher;
import org.springframework.amqp.core.*;
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
 * @date: 2020年12月29日 17:50
 * @description:
 */
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class,RabbitAutoConfiguration.class})
@ImportResource("classpath:xml/spring-ribbatmq-topic.xml")
public class PublisherTopic {
    private static RabbitTemplate rabbitTemplate;
    private static ConnectionFactory connectionFactory;
    private static AmqpAdmin amqpAdmin;
    private static TopicExchange topicExchange;
    @Autowired
    @Qualifier("amqpTemplate")
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        PublisherTopic.rabbitTemplate = rabbitTemplate;
    }
    @Autowired
    @Qualifier("connectionFactory")
    public void setConnectionFactory(ConnectionFactory connectionFactory) {
        PublisherTopic.connectionFactory = connectionFactory;
    }
    @Autowired
    @Qualifier("admin")
    public void setAmqpAdmin(AmqpAdmin amqpAdmin) {
        PublisherTopic.amqpAdmin = amqpAdmin;
    }
    @Autowired
    @Qualifier("topic-exchange")
    public void setTopicExchange(TopicExchange topicExchange) {
        PublisherTopic.topicExchange = topicExchange;
    }

    public static void main(String[] args) {
        SpringApplication.run(PublisherTopic.class, args);
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setAppId("publishTopic");
        messageProperties.setHeader("solr","ppt");
        /**
         * 发送数据
         * 第一个参数routingKey，第二个参数消息体
         */
        for (int j = 0; j < 10; j++) {
            Message message = new Message(("topic1 send info:"+j+"").getBytes(),messageProperties);
            rabbitTemplate.convertAndSend("topic1.asd", message);
        }
        for (int j = 0; j < 10; j++) {
            Message message = new Message(("topic1 send info:"+j+"").getBytes(),messageProperties);
            rabbitTemplate.convertAndSend("topic1.asd.esdf", message);
        }
        for (int j = 0; j < 10; j++) {
            Message message = new Message(("topic2 send info:"+j+"").getBytes(),messageProperties);
            rabbitTemplate.convertAndSend("topic2.aa",message);

        }
        for (int j = 0; j < 10; j++) {
            Message message = new Message(("topic2 send info:"+j+"").getBytes(),messageProperties);
            rabbitTemplate.convertAndSend("topic2.aa.333",message);

        }
    }

}
