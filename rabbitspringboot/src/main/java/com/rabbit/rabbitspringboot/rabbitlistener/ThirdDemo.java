package com.rabbit.rabbitspringboot.rabbitlistener;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @title：
 * @author: mengchaob
 * @date: 2021年01月12日 16:25
 * @description:
 */
@SpringBootApplication
public class ThirdDemo {
    public static void main(String[] args) {
        SpringApplication.run(ThirdDemo.class, args);
    }
    @Autowired
    public RabbitAdmin rabbitAdmin;
    @Autowired
    public Queue queue;
    @Autowired
    public DirectExchange directExchange;

    @Bean
    public ApplicationRunner runner(@Qualifier("cachingConnectionFactory") CachingConnectionFactory cachingConnectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        rabbitTemplate.setExchange(RabbitListenerUtil.directExchangeNameThird);
        /**
         * 绑定路由交换机、队列和routingKey
         */
        /**
         * 第一个目的名称
         * 第二个目标类型
         * 第三个交换机名称
         * 第四个路由名称
         */
        Binding binding = new Binding(queue.getName(),Binding.DestinationType.QUEUE,
                directExchange.getName(),RabbitListenerUtil.routingKeyThird,null);
        rabbitAdmin.declareBinding(binding);
        return new ThirdDemo.ApplicationRunnerImpl(rabbitTemplate);
    }

    static class ApplicationRunnerImpl implements ApplicationRunner {
        RabbitTemplate rabbitTemplate;
        public ApplicationRunnerImpl(RabbitTemplate rabbitTemplate) {
            this.rabbitTemplate = rabbitTemplate;
        }

        @Override
        public void run(ApplicationArguments args) throws Exception {
            for (int i = 0; i < 10; i++) {
                /**
                 * 第一个参数，没有设置交换机所以默认为队列名称
                 * 第二个参数，发送信息
                 */
                rabbitTemplate.convertAndSend(RabbitListenerUtil.routingKeyThird, "ThirdDemo send info" + i);
            }
        }
    }
    @RabbitListener(queues = RabbitListenerUtil.queueNameThird)
    public void listen(String info) {
        System.out.println("ThirdDemo 接收到的消息："+info);
    }
    @Bean
    public DirectExchange directExchange(CachingConnectionFactory cachingConnectionFactory){
        DirectExchange directExchange = new DirectExchange(RabbitListenerUtil.directExchangeNameThird,true,false);
        return directExchange;
    }

    @Bean
    public Queue queue(){
        Queue queue = new Queue(RabbitListenerUtil.queueNameThird);
        return queue;
    }
    @Bean
    public RabbitAdmin rabbitAdmin(CachingConnectionFactory cachingConnectionFactory){
        RabbitAdmin rabbitAdmin = new RabbitAdmin(cachingConnectionFactory);
        return rabbitAdmin;
    }
}
