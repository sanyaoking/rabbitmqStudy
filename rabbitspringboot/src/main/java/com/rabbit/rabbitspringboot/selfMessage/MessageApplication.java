package com.rabbit.rabbitspringboot.selfMessage;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.MessageMapping;

import java.util.List;

/**
 * @title：
 * @author: mengchaob
 * @date: 2021年01月11日 16:49
 * @description:
 */
@SpringBootApplication/*(exclude = {RabbitAutoConfiguration.class})*/
public class MessageApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessageApplication.class, args);
    }
    /**
     * 调用ApplicationRunner，springboot启动后自动执行对应的函数，发送信息到rabbitMq
     * @param cachingConnectionFactory
     * @return
     */
    @Bean
    public ApplicationRunner runner(@Qualifier("cachingConnectionFactory") CachingConnectionFactory cachingConnectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        return new ApplicationRunnerImpl(rabbitTemplate);
    }

    static class ApplicationRunnerImpl implements ApplicationRunner{
        RabbitTemplate rabbitTemplate;
        public ApplicationRunnerImpl(RabbitTemplate rabbitTemplate){
            this.rabbitTemplate = rabbitTemplate;
        }
        @Override
        public void run(ApplicationArguments args) throws Exception {
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setHeader("messageProperties","messageProperties");
            rabbitTemplate.setExchange(MessageUtil.messageExchangeName);
            rabbitTemplate.setRoutingKey(MessageUtil.routingKey);
            for (int i = 0; i < 10; i++) {
                MyMessage myMessage = new MyMessage("MessageApplication 发送的消息为："+i,messageProperties);
                rabbitTemplate.convertAndSend(myMessage);
            }
        }
    }

    /**
     * 使用监听主机的话，需要注意接口的参数类型必须同发送方发送的类型相同
     * @param in
     */
    @RabbitListener(queues = MessageUtil.queueName)
    public void listen(MyMessage in) {
        System.out.println("接收到的信息:"+in.toString());
    }
    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(@Qualifier("cachingConnectionFactory") CachingConnectionFactory cachingConnectionFactory){
        SimpleMessageListenerContainer container = new
                SimpleMessageListenerContainer();
        container.setConnectionFactory(cachingConnectionFactory);
        container.setQueueNames(MessageUtil.queueName);
        container.setMessageListener(new MessageListenerAdapter(new HelloWorldHandler()));
        return container;
    }

    /**
     * 注意handleMessage方法的参数类型，必须同发送时的类型相同
     */
    static class HelloWorldHandler {
        public void handleMessage(MyMessage text) {
            System.out.println("MessageApplication Received: " + text.toString());
        }
    }


}

