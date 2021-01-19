package com.rabbit.rabbitspringboot.delaySend;

import com.rabbit.rabbitspringboot.rabbitlistener.RabbitListenerUtil;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @title：
 * @author: mengchaob
 * @date: 2021年01月18日 10:34
 * @description:
 * 使用延迟发送信息，一定要注意，交换机必须设置delay=true; directExchange.setDelayed(true);
 */
@SpringBootApplication
public class DelaySendPubliser {
    public static void main(String[] args) {
        SpringApplication.run(DelaySendPubliser.class, args);
    }
    /**
     * 调用ApplicationRunner，springboot启动后自动执行对应的函数，发送信息到rabbitMq
     * @param cachingConnectionFactory
     * @return
     */
    @Bean
    public ApplicationRunner runner(@Qualifier("cachingConnectionFactory") CachingConnectionFactory cachingConnectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        return new DelaySendPubliser.ApplicationRunnerImpl(rabbitTemplate);
    }

    static class ApplicationRunnerImpl implements ApplicationRunner{
        RabbitTemplate rabbitTemplate;
        public ApplicationRunnerImpl(RabbitTemplate rabbitTemplate){
            this.rabbitTemplate = rabbitTemplate;
        }
        @Override
        public void run(ApplicationArguments args) throws Exception {
            /**
             * 设置延迟发送的时间
             */
            MessageProperties properties = new MessageProperties();
            properties.setDelay(150000);
            rabbitTemplate.send(DelaySendUtil.EXCHANGNAME, DelaySendUtil.ROUNTINGKEY,
                    MessageBuilder.withBody("foo".getBytes()).andProperties(properties).build
                            ());

            rabbitTemplate.convertAndSend("this is ReturnPubliser send info:");
        }
    }
    /**
     * 示例中注解RabbitListener实现了队列和交换机的声明初始化，并绑定routingKey
     * @param info
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = DelaySendUtil.QUEUENAME, durable = "true"),
            exchange = @Exchange(value = DelaySendUtil.EXCHANGNAME, ignoreDeclarationExceptions =
                    "true",type="direct"),
            key = DelaySendUtil.ROUNTINGKEY)
    )
    public void onMessage(String info){
        System.out.println("接收到的信息："+info);
    }
}
