package com.spring.rabbitmq.xml.pubsub;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @title：
 * @author: mengchaob
 * @date: 2020年12月25日 14:12
 * @description: xml配置客户端监听，广播发布队列queue2消费者
 */
public class Queue2Consuer {
    public static void main(String[] args) throws Exception {
        //创建Spring的ApplicationContext
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:xml/spring-ribbatmq-pubsub-consumer.xml");
        /**
         * 获取AmqpAdmin管理类
         */
        AmqpAdmin amqpAdmin = ctx.getBean("admin" , AmqpAdmin.class);
        /**
         * 创建队列queue
         */
        Queue queue2 = new Queue("queue2");
        amqpAdmin.declareQueue(queue2);
        /**
         * xml中如下配置可以实现客户端监听队列
         *     <!--配置消费者监听-->
         *     <rabbit:connection-factory id="rabbitConnectionFactory" host="127.0.0.1"  password="top.mengchao.www" username="mengchaob" virtual-host="mengchaobhost"/>
         *     <rabbit:listener-container connection-factory="rabbitConnectionFactory">
         *         <rabbit:listener ref="queue2ReceiveMessage" queue-names="queue2"></rabbit:listener>
         *     </rabbit:listener-container>
         *     <bean id="queue2ReceiveMessage" class="com.spring.rabbitmq.xml.pubsub.Queue2ReceiveMessage"></bean>
         */

    }


}
