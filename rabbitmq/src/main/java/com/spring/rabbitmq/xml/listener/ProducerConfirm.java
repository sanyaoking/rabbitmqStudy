package com.spring.rabbitmq.xml.listener;

import com.spring.rabbitmq.xml.direct.PublisherDirect;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
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
 * @date: 2020年12月30日 19:19
 * @description:
 */
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class,RabbitAutoConfiguration.class})
@ImportResource("classpath:xml/spring-ribbatmq-confirm.xml")
public class ProducerConfirm {
    private static RabbitTemplate rabbitTemplate;
    @Autowired
    @Qualifier("amqpTemplate")
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        ProducerConfirm.rabbitTemplate = rabbitTemplate;
        ProducerConfirm.rabbitTemplate.setReturnCallback(new ReturnsCallbackImpl());
        ProducerConfirm.rabbitTemplate.setConfirmCallback(new ConfirmCallbackImpl());
    }
    public static void main(String[] args) {
        SpringApplication.run(ProducerConfirm.class, args);
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setAppId("publishDirect");
        messageProperties.setHeader("className","PublisherDirect");
        ProducerConfirm.rabbitTemplate.setMandatory(true);
        /**
         * returnsback测试,注意是没有这个队列的
         */
        for (int j = 0; j < 10; j++) {
            /**
             * 一定要创建此信息，并调用相应的重载方法，只有这样，对应的confirm回调方法才会被消费,
             * 通过这个例子可以可以看出虽然被路由接收但是没有找到对应的key对应的队列，所以会触发ReturnCallback事件
             */
            CorrelationData correlationData = new CorrelationData();
            correlationData.setId("id"+j);
            Message message = new Message(("direct.topic send info:"+j+"").getBytes(),messageProperties);
            ProducerConfirm.rabbitTemplate.convertAndSend( "direct.key3",message,correlationData);
        }
        /**
         * confirm测试
         */
        for (int j = 0; j < 10; j++) {
            /**
             * 一定要创建此信息，并调用相应的重载方法，只有这样，对应的confirm回调方法才会被消费
             */
            CorrelationData correlationData = new CorrelationData();
            correlationData.setId("id"+j);
            Message message = new Message(("direct.topic send info:"+j+"").getBytes(),messageProperties);
            ProducerConfirm.rabbitTemplate.convertAndSend( "direct.key2",message,correlationData);
        }
    }
    static class ReturnsCallbackImpl implements RabbitTemplate.ReturnCallback{
        @Override
        public void returnedMessage(Message message, int i, String s, String s1, String s2) {
            System.out.println("被退回 returnedMessage！ start");
            System.out.print(message+"===");
            System.out.print(i+"===");
            System.out.print(s+"===");
            System.out.print(s1+"===");
            System.out.print(s2+"===");
            System.out.println("被退回 returnedMessage！ end");
        }
    }
    static class ConfirmCallbackImpl implements RabbitTemplate.ConfirmCallback{
        @Override
        public void confirm(CorrelationData correlationData, boolean b, String s) {
            System.out.println("被确认 confirm！ start");
            System.out.print(correlationData+"===");
            System.out.print(b+"===");
            System.out.print(s+"===");
            System.out.print("被确认 confirm！ end");
        }
    }
}
