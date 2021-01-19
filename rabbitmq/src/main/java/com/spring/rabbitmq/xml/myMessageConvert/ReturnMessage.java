package com.spring.rabbitmq.xml.myMessageConvert;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @title：
 * @author: mengchaob
 * @date: 2021年01月04日 10:22
 * @description:
 */
public class ReturnMessage {
    public static void main(String[] args) {
        //创建Spring的ApplicationContext
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:xml/spring-ribbatmq-consumer-returnmessage.xml");
        RabbitTemplate rabbitTemplate = ctx.getBean("amqpTemplate" , RabbitTemplate.class);
        rabbitTemplate.setAfterReceivePostProcessors(new MyMessagePostProcessor());
        boolean flag = rabbitTemplate.receiveAndReply("returnmessage_queue1",new ReceiveAndReplyCallbackImple(),"direct-returnmessage", "message.reply.key");
        if(flag){
            System.out.println("消费成功！");
        }else {
            System.out.println("消费失败！");
        }
    }
}
/**
 *
 */

/**
 * MyMessage发送信息时选用的对象
 * MyReplyMessage 自己设置的返回类型
 */
 class  ReceiveAndReplyCallbackImple implements ReceiveAndReplyCallback<MyMessage, MyReplyMessage> {
     @Override
     public MyReplyMessage handle(MyMessage message) {
         System.out.println("收到的信息："+message);
         MessageProperties messageProperties = new MessageProperties();
         messageProperties.setAppId("pubsub");
         messageProperties.setHeader("solr","ppt");
         messageProperties.setCorrelationId("receiveAndReplyCallbackImple");
         MyReplyMessage myReplyMessage = new MyReplyMessage("this is reply'info",messageProperties);
         return myReplyMessage;
     }
 }
