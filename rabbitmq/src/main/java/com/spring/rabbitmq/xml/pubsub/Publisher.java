package com.spring.rabbitmq.xml.pubsub;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.impl.AMQImpl;
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
 * @date: 2020年12月23日 16:12
 * @description:
 */
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class,RabbitAutoConfiguration.class})
@ImportResource("classpath:xml/spring-ribbatmq-pubsub.xml")
public class Publisher {
    private static RabbitTemplate rabbitTemplate;
    private static ConnectionFactory connectionFactory;
    private static AmqpAdmin amqpAdmin;
    private static FanoutExchange fanoutExchange;
    @Autowired
    @Qualifier("amqpTemplate")
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        Publisher.rabbitTemplate = rabbitTemplate;
    }
    @Autowired
    @Qualifier("connectionFactory")
    public void setConnectionFactory(ConnectionFactory connectionFactory) {
        Publisher.connectionFactory = connectionFactory;
    }
    @Autowired
    @Qualifier("admin")
    public void setAmqpAdmin(AmqpAdmin amqpAdmin) {
        Publisher.amqpAdmin = amqpAdmin;
    }
    @Autowired
    @Qualifier("my_fanout_exchange")
    public void setFanoutExchange(FanoutExchange fanoutExchange) {
        Publisher.fanoutExchange = fanoutExchange;
    }

    public static void main(String[] args) {
        SpringApplication.run(Publisher.class, args);
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setAppId("pubsub");
        messageProperties.setHeader("solr","ppt");
        /*try {
            *//**
             * 创建队列
             *//*
            Publisher.connectionFactory.createConnection().createChannel(false).queueDeclare("queue2", true, false, false, null);
            *//*绑定队列和交换机*//*
            Publisher.connectionFactory.createConnection().createChannel(false).queueBind("queue2","my_fanout_exchange","");
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        /**
         * 创建队列
         */
        Queue queue2 = new Queue("queue2");
        amqpAdmin.declareQueue(queue2);
        /**
         * 创建绑定队列和交换机的Binding对象
         */
        Binding bind = BindingBuilder.bind(queue2).to(fanoutExchange);
        /**
         * 开始绑定
         */
        amqpAdmin.declareBinding(bind);
        /**
         * 模板绑定交换机
         */
        rabbitTemplate.setExchange("my_fanout_exchange");
        /**
         * 发送数据
         * 第一个参数routingKey，第二个参数消息体
         */
        for (int j = 0; j < 10; j++) {
            Message message = new Message(("pubsub send info:"+j+"").getBytes(),messageProperties);
            rabbitTemplate.convertAndSend( message);

        }
    }
}
