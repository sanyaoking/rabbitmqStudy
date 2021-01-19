package com.rabbit.rabbitspringboot.header;

import com.rabbit.rabbitspringboot.selfMessage.MyMessage;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
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
 * 暂时找到的方法只能用Message实体类进行传输！！
 */
@SpringBootApplication/*(exclude = {RabbitAutoConfiguration.class})*/
public class HeadersApplication {

    public static void main(String[] args) {
        SpringApplication.run(HeadersApplication.class, args);
    }
    /**
     * 调用ApplicationRunner，springboot启动后自动执行对应的函数，发送信息到rabbitMq
     * @param cachingConnectionFactory
     * @return
     */
    @Bean
    public ApplicationRunner runner(@Qualifier("cachingConnectionFactory") CachingConnectionFactory cachingConnectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        return new ApplicationRunnerImpl(rabbitTemplate);
    }

    static class ApplicationRunnerImpl implements ApplicationRunner{
        RabbitTemplate rabbitTemplate;
        public ApplicationRunnerImpl(RabbitTemplate rabbitTemplate){
            this.rabbitTemplate = rabbitTemplate;
        }
        @Override
        public void run(ApplicationArguments args) throws Exception {
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setHeader("key1","foot");
            rabbitTemplate.setExchange(HeaderUtil.headersExchangeName);
            rabbitTemplate.setRoutingKey(HeaderUtil.routingKey);
            for (int i = 0; i < 10; i++) {
                Message myMessage = new Message(("MessageApplication 发送的消息为：key1=foot_"+i).getBytes(),messageProperties);
                rabbitTemplate.convertAndSend(myMessage);
            }

            MessageProperties messageProperties1 = new MessageProperties();
            messageProperties1.setHeader("key2","qqqq");
            messageProperties1.setHeader("key1","foot");
            for (int i = 0; i < 10; i++) {
                Message myMessage = new Message(("MessageApplication 发送的消息为：key1=foot,key2=qqqq,info="+i).getBytes(),messageProperties1);
                rabbitTemplate.convertAndSend(myMessage);
            }
            /**
             * 按key2=qqqq的规则发送数据
             */
            MessageProperties messageProperties2 = new MessageProperties();
            messageProperties2.setHeader("key2","qqqq");
            messageProperties2.setHeader("key1","123");
            for (int i = 0; i < 10; i++) {
                Message myMessage = new Message(("MessageApplication 发送的消息为：key1=123，key2=qqqq;info="+i).getBytes(),messageProperties2);
                rabbitTemplate.convertAndSend(myMessage);
            }

            /**
             * 下面的列子无法发送到rouotingKey所对应的队列中去
             */
            MessageProperties messageProperties3 = new MessageProperties();
            messageProperties2.setHeader("key1","qqqq");
            for (int i = 0; i < 10; i++) {
                Message myMessage = new Message(("MessageApplication 发送的消息为：key1=qqqq_"+i).getBytes(),messageProperties3);
                rabbitTemplate.convertAndSend(myMessage);
            }
        }
    }

    /**
     * 使用监听主机的话，需要注意接口的参数类型必须同发送方发送的类型相同
     * @param in
     */
    @RabbitListener(queues = HeaderUtil.queueName)
    public void listen(String in) {
        System.out.println("接收到的信息:"+in);
    }
}

