package com.spring.rabbitmq.xml.direct;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import java.util.List;

/**
 * @title：
 * @author: mengchaob
 * @date: 2020年12月29日 23:25
 * @description:
 */
public class directQueueReceiveMessage implements MessageListener {
    @Override
    public void onMessage(Message message) {
        System.out.println("directQueueReceiveMessage onMessage:"+message);
    }

    @Override
    public void containerAckMode(AcknowledgeMode mode) {
        System.out.println("directQueueReceiveMessage containerAckMode:"+mode);
    }

    @Override
    public void onMessageBatch(List<Message> messages) {
        System.out.println("directQueueReceiveMessage containerAckMode:"+messages);
    }
}
