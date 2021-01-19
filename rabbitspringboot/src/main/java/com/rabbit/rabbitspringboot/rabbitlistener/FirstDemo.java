package com.rabbit.rabbitspringboot.rabbitlistener;

import com.rabbit.rabbitspringboot.routingConnectionFactory.RoutingFactoryApplication;
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
 * @date: 2021年01月12日 15:49
 * @description:
 */
//@SpringBootApplication
public class FirstDemo {
    public static void main(String[] args) {
        SpringApplication.run(FirstDemo.class, args);
    }

    @Bean
    public ApplicationRunner runner(@Qualifier("cachingConnectionFactory") CachingConnectionFactory cachingConnectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        rabbitTemplate.setRoutingKey(RabbitListenerUtil.routingKey);
        rabbitTemplate.setExchange(RabbitListenerUtil.directExchangeName);
        return new FirstDemo.ApplicationRunnerImpl(rabbitTemplate);
    }

    static class ApplicationRunnerImpl implements ApplicationRunner{
        RabbitTemplate rabbitTemplate;
        public ApplicationRunnerImpl(RabbitTemplate rabbitTemplate){
            this.rabbitTemplate = rabbitTemplate;
        }
        @Override
        public void run(ApplicationArguments args) throws Exception {
            for (int i = 0; i < 10; i++) {
                /**
                 * 第一个参数，没有设置交换机所以默认为队列名称
                 * 第二个参数，发送信息
                 */
                rabbitTemplate.convertAndSend(RabbitListenerUtil.routingKey,"FirstDemo send info"+i);
            }

        }
    }

    /**
     * 示例中注解RabbitListener实现了队列和交换机的声明初始化，并绑定routingKey
     * @param info
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = RabbitListenerUtil.queueName, durable = "true"),
            exchange = @Exchange(value = RabbitListenerUtil.directExchangeName, ignoreDeclarationExceptions =
                    "true",type="direct"),
            key = RabbitListenerUtil.routingKey)
    )
    public void processOrder(String info) {
        System.out.println("FirstDemo 接收到的消息："+info);
    }
}
