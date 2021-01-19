package com.rabbit.rabbitspringboot.rabbitlistener;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
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
public class SecondDemo {
    public static void main(String[] args) {
        SpringApplication.run(SecondDemo.class, args);
    }

    @Bean
    public ApplicationRunner runner(@Qualifier("cachingConnectionFactory") CachingConnectionFactory cachingConnectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        rabbitTemplate.setExchange(RabbitListenerUtil.directExchangeNameSecond);
        return new SecondDemo.ApplicationRunnerImpl(rabbitTemplate);
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
                rabbitTemplate.convertAndSend("","SecondDemo send info"+i);
            }

        }
    }

    /**
     * key=""绑定的空key，和@Queue，实现监听默认队列，需要注意，发送的时候，routingKey要为空
     * 这种注解方式@Queue会创建一个默认的队列,队列名称为随机字符串，当客户端停掉时会自从删除这个队列
     * @param info
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue,
            exchange = @Exchange(value = RabbitListenerUtil.directExchangeNameSecond, ignoreDeclarationExceptions =
                    "true",type="direct"),
            key = "")
    )
    public void processOrder(String info) {
        System.out.println("SecondtDemo 接收到的消息："+info);
    }
}
