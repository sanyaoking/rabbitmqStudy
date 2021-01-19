package com.spring.rabbitmq.xml.pubsub;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import java.util.List;

/**
 * @title：
 * @author: mengchaob
 * @date: 2020年12月26日 23:59
 * @description:
 */
public class Queue2ReceiveMessage implements MessageListener {
    @Override
    public void onMessage(Message message) {
        System.out.println("Queue2ReceiveMessage 监听生效了，收到的消息:"+message);
    }

    @Override
    public void containerAckMode(AcknowledgeMode mode) {
        System.out.println("Queue2ReceiveMessage 监听生效了，containerAckMode 收到的信息:"+mode);
    }

    @Override
    public void onMessageBatch(List<Message> messages) {
        System.out.println("Queue2ReceiveMessage 监听生效了，onMessageBatch 收到的消息:"+messages);
    }
}
