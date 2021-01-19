package com.rabbit.rabbitspringboot.routingConnectionFactory;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.SimpleResourceHolder;
import org.springframework.amqp.rabbit.connection.SimpleRoutingConnectionFactory;
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
 * @date: 2021年01月11日 16:49
 * @description:
 */
@SpringBootApplication/*(exclude = {RabbitAutoConfiguration.class})*/
public class RoutingFactoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoutingFactoryApplication.class, args);
    }
    /**
     * 调用ApplicationRunner，springboot启动后自动执行对应的函数，发送信息到rabbitMq
     * @param simpleRoutingConnectionFactory
     * @return
     */
    @Bean
    public ApplicationRunner runner(@Qualifier("simpleRoutingConnectionFactory") SimpleRoutingConnectionFactory simpleRoutingConnectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(simpleRoutingConnectionFactory);
        return new ApplicationRunnerImpl(rabbitTemplate);
    }

    static class ApplicationRunnerImpl implements ApplicationRunner{
        RabbitTemplate rabbitTemplate;
        public ApplicationRunnerImpl(RabbitTemplate rabbitTemplate){
            this.rabbitTemplate = rabbitTemplate;
        }
        @Override
        public void run(ApplicationArguments args) throws Exception {
            SimpleResourceHolder.bind(rabbitTemplate.getConnectionFactory(), "factory1");
            /**
             * 第一个参数，没有设置交换机所以默认为队列名称
             * 第二个参数，发送信息
             */
            rabbitTemplate.convertAndSend("queueRouting","send factory1 info");
            SimpleResourceHolder.unbind(rabbitTemplate.getConnectionFactory());
            SimpleResourceHolder.bind(rabbitTemplate.getConnectionFactory(), "factory2");
            rabbitTemplate.convertAndSend("queueRouting","send factory2 info");
            SimpleResourceHolder.unbind(rabbitTemplate.getConnectionFactory());
        }
    }

    @RabbitListener(containerFactory="rabbitListenerContainerFactory1",queues = "queueRouting")
    public void listen1(String in) {
        System.out.println("factory1:"+in);
    }
    @RabbitListener(containerFactory="rabbitListenerContainerFactory2",queues = "queueRouting")
    public void listen(String in) {
        System.out.println("factory2:"+in);
    }
}
