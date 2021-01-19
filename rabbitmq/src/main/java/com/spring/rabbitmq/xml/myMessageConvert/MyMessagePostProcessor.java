package com.spring.rabbitmq.xml.myMessageConvert;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Correlation;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;

/**
 * 接受消息后调用
 */
class MyMessagePostProcessor implements MessagePostProcessor {
    @Override
    public Message postProcessMessage(Message message) throws AmqpException {
        System.out.println("调用方法MyMessagePostProcessor postProcessMessage(Message message)" + message);
        return message;
    }

    @Override
    public Message postProcessMessage(Message message, Correlation correlation) {
        System.out.println("调用方法MyMessagePostProcessor postProcessMessage(Message message, Correlation correlation) " +message);
        return message;
    }
}
