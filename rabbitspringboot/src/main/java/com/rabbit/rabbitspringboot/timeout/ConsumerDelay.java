package com.rabbit.rabbitspringboot.timeout;

import com.rabbit.rabbitspringboot.ConnectionUtil;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;

/**
 * @title：
 * @author: mengchaob
 * @date: 2021年01月09日 15:36
 * @description:
 */
public class ConsumerDelay {
    public static void main(String[] args) {
        SimpleMessageListenerContainer container = new
                SimpleMessageListenerContainer();
        container.setConnectionFactory(ConnectionUtil.cachingConnectionFactory);
        container.setQueueNames(DelayConfig.queueName);
        container.setMessageListener(message -> {
            System.out.println(message.toString());
        });
        container.start();
    }
}
