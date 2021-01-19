package com.spring.rabbitmq.xml.hellow;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Correlation;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.*;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @title：
 * @author: mengchaob
 * @date: 2020年12月21日 22:56
 * @description:
 */
public class Consume {
    public static void main(String[] args) throws Exception {
        //创建Spring的ApplicationContext
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:xml/spring-ribbatmq-hellow.xml");
        RabbitTemplate rabbitTemplate = ctx.getBean("amqpTemplate" , RabbitTemplate.class);
        /**
         * 第一个参数routingKey，第二个参数消息体
         */
        rabbitTemplate.addAfterReceivePostProcessors(new MyMessagePostProcessor());
        reveiveOnce(rabbitTemplate);
    }

    public static void reveiveOnce(RabbitTemplate rabbitTemplate){
        //每次获取一条
        Object obj = rabbitTemplate.receiveAndConvert("helloWorld");
        if(obj!=null) {
            String receive = (String) obj;
            System.out.println("接受到的消息：" + receive.toString());
        }
    }

}

 class MyMessagePostProcessor implements MessagePostProcessor {
     @Override
     public Message postProcessMessage(Message message) throws AmqpException {
         System.out.println("调用方法MyMessagePostProcessor postProcessMessage(Message message)" + message);
         return message;
     }

     @Override
     public Message postProcessMessage(Message message, Correlation correlation) {
         System.out.println("调用方法MyMessagePostProcessor postProcessMessage(Message message, Correlation correlation) " +message);
         return message;
     }
 }