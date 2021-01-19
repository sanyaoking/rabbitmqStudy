package com.spring.rabbitmq.xml.myMessageConvert;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

import java.lang.reflect.Type;

/**
 * @title：
 * @author: mengchaob
 * @date: 2021年01月07日 23:02
 * @description:
 */
class MessageConverterImpl implements MessageConverter {
    @Override
    public Message toMessage(Object o, MessageProperties messageProperties) throws MessageConversionException {
        if(o==null){
            return null;
        }
        MyMessage myMessage = (MyMessage)o;
        Message message = new Message(myMessage.body.getBytes(),myMessage.messageProperties);
        return message;
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        MyMessage myMessage = new MyMessage(new String(message.getBody()),message.getMessageProperties());
        return myMessage;
    }

    @Override
    public Message toMessage(Object object, MessageProperties messageProperties, Type genericType) throws MessageConversionException {
        if(object==null){
            return null;
        }
        MyMessage myMessage = (MyMessage)object;
        Message message = new Message(myMessage.body.getBytes(),myMessage.messageProperties);
        return message;
    }
}
