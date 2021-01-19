package com.rabbit.rabbitspringboot.direct;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.SimpleResourceHolder;
import org.springframework.amqp.rabbit.connection.SimpleRoutingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @title：
 * @author: mengchaob
 * @date: 2021年01月11日 16:49
 * @description:
 */
@SpringBootApplication/*(exclude = {RabbitAutoConfiguration.class})*/
public class DirectApplication {

    public static void main(String[] args) {
        SpringApplication.run(DirectApplication.class, args);
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
            rabbitTemplate.setExchange(DirectUtil.directExchangeName);
            rabbitTemplate.setRoutingKey(DirectUtil.routingKey);
            for (int i = 0; i < 10; i++) {
                Message message = new Message(("DirectApplication 发送的消息为："+i).getBytes(),messageProperties);
                rabbitTemplate.send(message);
            }
        }
    }

    /**
     * 这个注解绑定了路由器、队列和routingkey。并且设置了监听方法
     * @param in
     */
    @RabbitListener(
            bindings = @QueueBinding( value = @Queue(value = DirectUtil.queueName, durable = "true"),
            exchange = @Exchange(value =DirectUtil.directExchangeName, ignoreDeclarationExceptions = "true"),
                    key = DirectUtil.routingKey))
//    @RabbitListener(queues = DirectUtil.queueName)
    public void listen(String in) {
        System.out.println("接收到的信息:"+in);
    }
}
