package com.spring.rabbitmq.xml.reply;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;

import java.util.List;

/**
 * @title：
 * @author: mengchaob
 * @date: 2021年01月07日 12:05
 * @description:
 */
public class ConsumerContainerReply {
    public static void main(String[] args) {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setHost("localhost");
        factory.setVirtualHost("mengchaobhost");
        factory.setPassword("top.mengchao.www");
        factory.setUsername("mengchaob");
        RabbitTemplate rabbitTemplate = new RabbitTemplate(factory);
        rabbitTemplate.setUseDirectReplyToContainer(false);
        rabbitTemplate.setExchange("direct-exchange");
        rabbitTemplate.setRoutingKey("reply.key1");
        SimpleMessageListenerContainer container = new
                SimpleMessageListenerContainer();
        container.setConnectionFactory(factory);
        container.setQueues(new Queue("dead_queue1"));
        container.setMessageListener(new MessageListenerImpl(rabbitTemplate));
        container.start();
    }
    static class MessageListenerImpl implements MessageListener {
        private RabbitTemplate rabbitTemplate;
        public MessageListenerImpl(RabbitTemplate rabbitTemplate){
            this.rabbitTemplate = rabbitTemplate;
        }
        @Override
        public void onMessage(Message message) {
            System.out.println("收到的信息："+ new String(message.getBody()));
            new Message(("this is reply'info,收到的信息："+ new String(message.getBody())).getBytes(),message.getMessageProperties());
            this.rabbitTemplate.convertAndSend(message);
        }

        @Override
        public void containerAckMode(AcknowledgeMode mode) {
            System.out.println("监听生效了，containerAckMode 收到的信息:"+mode);
        }

        @Override
        public void onMessageBatch(List<Message> messages) {
            System.out.println("监听生效了，onMessageBatch 收到的消息:"+messages);
        }
    }
}
