package com.spring.rabbitmq.xml.myMessageConvert;

import org.springframework.amqp.core.MessageProperties;

import java.io.Serializable;

/**
 * @title：
 * @author: mengchaob
 * @date: 2021年01月04日 12:11
 * @description:
 */
public class MyReplyMessage  implements Serializable {
    String body = "";
    MessageProperties messageProperties;

    public MyReplyMessage(String body, MessageProperties messageProperties) {
        this.body = body;
        this.messageProperties = messageProperties;
    }

    @Override
    public String toString() {
        return "body"+body+"||messageProperties:"+messageProperties;
    }
}
