package com.spring.rabbitmq.xml.dead;

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
 * @date: 2021年01月08日 11:47
 * @description:
 */
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class,RabbitAutoConfiguration.class})
@ImportResource("classpath:xml/spring-ribbatmq-dead.xml")
public class Publisher {
    private static RabbitTemplate rabbitTemplate;
    @Autowired
    @Qualifier("rabbitTemplate")
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        Publisher.rabbitTemplate = rabbitTemplate;

    }
    public static void main(String[] args) {
        /**
         * 向一个正常队列发送数据，然后消费删除这个数据，则数据会被放到dead_queue2队列中去
         */
        SpringApplication.run(Publisher.class, args);
        MessageProperties mp = new MessageProperties();
        for (int i = 0; i < 10; i++) {
            Message message = new Message((i+"").getBytes(),mp);
            rabbitTemplate.send(message);
        }

    }
}
