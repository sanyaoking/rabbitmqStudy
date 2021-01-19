package com.rabbit.rabbitspringboot.delay1;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @title：
 * @author: mengchaob
 * @date: 2021年01月11日 11:25
 * @description: 注意启动制定不同的配置文件
 */
@SpringBootApplication
public class DelayPublisherAndConsumer {
    public static void main(String[] args) {
        SpringApplication.run(DelayPublisherAndConsumer.class, args);
    }

    /**
     * 调用ApplicationRunner，springboot启动后自动执行对应的函数，发送信息到rabbitMq
     * @param template
     * @return
     */
    @Bean
    public ApplicationRunner runner(AmqpTemplate template) {
        return args -> template.convertAndSend(DelayConfig.queueName, "foo");
    }

    /**
     * 接收timeoutqueue-queue-boot队列由于超时而进入到死信队列dead-queue-boot中的信息
     * @param in
     */
    @RabbitListener(queues = DelayConfig.queueDeadName)
    public void listen(String in) {
        System.out.println(DelayConfig.queueDeadName+"接收到信息info"+in);
    }
}
