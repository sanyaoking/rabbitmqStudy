package com.rabbit.rabbitspringboot.timeout;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @title：
 * @author: mengchaob
 * @date: 2021年01月09日 14:38
 * @description:
 * new Binding(someQueue, someDirectExchange, "foo.bar");
 * new Binding(someQueue, someFanoutExchange);
 * new Binding(someQueue, someTopicExchange, "foo.*");
 * 流式api
 * Binding b = BindingBuilder.bind(someQueue).to(someTopicExchange).with("foo.*");
 *
 * 超时自动删除信息，需要配置在队列上，这个是队列属性
 */
@SpringBootApplication
public class ProductDelay {
    private static DirectExchange directExchange;
    private static Queue queue;
    private static RabbitTemplate rabbitTemplate;
    private static RabbitAdmin rabbitAdmin;
    @Autowired
    public void setDirectExchange(DirectExchange directExchange) {
        this.directExchange = directExchange;
    }
    @Autowired
    public void setQueue(Queue queue) {
        this.queue = queue;
    }
    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    @Autowired
    public void setRabbitAdmin(RabbitAdmin rabbitAdmin) {
        ProductDelay.rabbitAdmin = rabbitAdmin;
    }

    public static void main(String[] args) {
        SpringApplication.run(ProductDelay.class, args);
        rabbitAdmin.declareExchange(directExchange);
        rabbitAdmin.declareQueue(queue);
        /**
         * 第一个目的名称
         * 第二个目标类型
         * 第三个交换机名称
         * 第四个路由名称
         */
        Binding binding = new Binding(queue.getName(),Binding.DestinationType.QUEUE,
                directExchange.getName(),DelayConfig.routing_key,null);
        rabbitAdmin.declareBinding(binding);
        MessageProperties messageProperties = new MessageProperties();
        for (int i = 0; i < 10; i++) {
            Message message = new Message((i+"").getBytes(),messageProperties);
            rabbitTemplate.convertAndSend(directExchange.getName(),DelayConfig.routing_key,message);
        }


    }
}
