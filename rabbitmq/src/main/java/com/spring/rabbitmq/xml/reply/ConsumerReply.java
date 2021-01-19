package com.spring.rabbitmq.xml.reply;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.ReceiveAndReplyCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @title：
 * @author: mengchaob
 * @date: 2020年12月29日 23:21
 * @description:
 */
public class ConsumerReply {
    public static void main(String[] args) {
        //创建Spring的ApplicationContext
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:xml/spring-ribbatmq-reply-consumer.xml");
        RabbitTemplate rabbitTemplate = ctx.getBean("amqpTemplate" , RabbitTemplate.class);
        rabbitTemplate.setMandatory(false);
        int i =0;
        while(i<60) {
            /**
             * 收到消息后，会通过direct-exchange路由+reply.key1关键字转发到对应的队列去
             */
            boolean flag = rabbitTemplate.receiveAndReply("dead_queue1", new ReceiveAndReplyCallbackImple(), "direct-exchange", "reply.key1");
            if (flag) {
                System.out.println("消费成功！");
            } else {
                System.out.println("消费失败！");
            }
            i++;
        }
    }

    /**
     * byte[] 未发送的数据内容信息
     * Message和byt[]都可以替换成其他类型，但是需要注意的是，发送方也要做相应修改
     */
    static class  ReceiveAndReplyCallbackImple implements ReceiveAndReplyCallback<byte[], Message> {
        @Override
        public Message handle(byte[] message) {
            String mess = new String(message);
            System.out.println("收到的信息："+mess);
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setAppId("pubsub");
            messageProperties.setHeader("solr","ppt");
            Message myReplyMessage = new Message(("this is reply'info,收到的信息："+mess).getBytes(),messageProperties);
            return myReplyMessage;
        }
    }

}
