package com.spring.rabbitmq.xml.reply;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.SuccessCallback;

/**
 * @title：
 * @author: mengchaob
 * @date: 2020年12月29日 23:19
 * @description:
 * reply可以异步接受消费者或者生产者对消息的消费信息，前提是需要对应的生产者或消费者作出对应的设置
 * AsyncRabbitTemplate生产者可以通过sendAndReceive来异步接受消费者返回信息，RabbitTemplate消费者可以通过receiveAndReply来异步回复消费信息
 */
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class,RabbitAutoConfiguration.class})
@ImportResource("classpath:xml/spring-ribbatmq-reply.xml")
public class PublisherReply {
    /**
     * 注意rabbitTemplate，类型为AsyncRabbitTemplate
     */
    private static AsyncRabbitTemplate asyncRabbitTemplate;
    @Autowired
    @Qualifier("amqpTemplate")
    public void setRabbitTemplate(AsyncRabbitTemplate rabbitTemplate) {
        PublisherReply.asyncRabbitTemplate = rabbitTemplate;
    }
    public static void main(String[] args) {
        /**
         * 做测试的时候一定先删除已有队列防止已有队列定义同本demo中队列定义不一致的情况发生
         */
        SpringApplication.run(PublisherReply.class, args);
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setHeader("className","PublisherDead");
        /**
         * 发送数据
         * 第一个参数routingKey，第二个参数消息体
         */
        for (int j = 0; j < 10; j++) {
            /**
             * CorrelationId必须存在，并且不同
             */
            messageProperties.setCorrelationId(j+"");
            Message message = new Message(("direct.key1 send info:" + j + "").getBytes(), messageProperties);
            AsyncRabbitTemplate.RabbitMessageFuture rabbitMessageFuture =  PublisherReply.asyncRabbitTemplate.sendAndReceive("dead.key1", message);
            rabbitMessageFuture.addCallback(new SuccessCallbackImpl(),new FailureCallbackImpl());
        }

    }
    static class SuccessCallbackImpl implements SuccessCallback {
        @Override
        public void onSuccess(Object o) {
            Message mess = (Message) o;
            String body = new String(mess.getBody());
            System.out.println("成功！"+body);
        }
    }
    static class FailureCallbackImpl implements FailureCallback{
        @Override
        public void onFailure(Throwable throwable) {
            System.out.println(throwable.getMessage());
            System.out.println("失败！");
        }
    }
}
