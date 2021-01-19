package top.mengchao.rabbitmq.listener;

import com.rabbitmq.client.*;
import top.mengchao.rabbitmq.Common;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Producer {

    public static String QUEQULIMIT = "commitqueue";
    public static String LISTENEREXCHANGE = "listenerexchange";

    public static void main(String[] argv) throws Exception {
        /**
         * 1. 创建connect
         * 2. 创建channel
         * 3. 绑定channel和队列
         * 4. 向队列发送数据
         */
        Connection connection = Common.getConnection();
        Channel channel = connection.createChannel();
        /**
         * 1.如果没有则创建，交换机
         * 第一个参数 交换机名字
         * 第二个参数 交换机类型
         * 第三个参数 是否持久化
         * 第四个参数 是否自动删除
         **/
        channel.exchangeDeclare(Producer.LISTENEREXCHANGE, "direct", true, false, null);
        /**
         * 开启Confirm监听模式，如果不开启的话addConfirmListener添加的监听无效
         */
        channel.confirmSelect();
        /**
         * 绑定confirm监听器
         */
        channel.addConfirmListener(new ConfirmListener() {
            public void handleAck(long l, boolean b) throws IOException {
                //第二个参数代表接收的数据是否为批量接收，一般我们用不到。
                System.out.println("消息已被Broker接收,Tag:" + l);
            }

            public void handleNack(long l, boolean b) throws IOException {
                System.out.println("消息已被Broker拒收,Tag:" + l);
            }
        });
        /**
         * 绑定退回监听器,
         */
        channel.addReturnListener(new ReturnCallback() {
            public void handle(Return r) {
                System.err.println("===========================");
                System.err.println("Return编码：" + r.getReplyCode() + "-Return描述:" + r.getReplyText());
                System.err.println("交换机:" + r.getExchange() + "-路由key:" + r.getRoutingKey());
                System.err.println("Return主题：" + new String(r.getBody()));
                System.err.println("===========================");
            }
        });
        channel.addReturnListener(new ReturnListener() {
            public void handleReturn(int replyCode,
                                     String replyText,
                                     String exchange,
                                     String routingKey,
                                     AMQP.BasicProperties properties,
                                     byte[] body)
                    throws IOException {
                System.out.println("replyCode:" + replyCode);
                System.out.println("replyText:" + replyText);
                System.out.println("exchange:" + exchange);
                System.out.println("routingKey:" + routingKey);
                System.out.println("body:" + new String(body));
            }
        });
        /**
         * 测试confirm
         * 第一个参数，是否持久化，
         * 第三个参数，是否自动删除
         */
        Map<String, Object> args = new HashMap<String, Object>();
        /**
         * 当前的消息放入的时候如果超过了队列的最大长度那么默认RabbitMQ的就是直接删除最前面的消息(即老的消息)或者将消息放入死信队列。可以通过溢出属性x-overflow设定并修改行为
         */
        args.put("x-max-length", 5);
        /**
         * 可以通过为x-overflow队列声明参数提供字符串值来设置溢出行为 。可能的值为drop-head（默认）或 reject-publish
         */
        args.put("x-overflow", "reject-publish");
        channel.queueDeclare(Producer.QUEQULIMIT, false, false, false, args);
        channel.queueBind(Producer.QUEQULIMIT, Producer.LISTENEREXCHANGE, "test");
        /**
         * 队列长度为5所以发送6个，前5个handleAck，最后一个handleNack
         */
        for (int i = 0; i < 6; i++) {
            //测试的时候不要开启消费着,且测试的时候如果没有调用Thread.sleep(1)或waitForConfirms或waitForConfirmsOrDie方法，有些信息的confirm会没有反馈
            channel.basicPublish(Producer.LISTENEREXCHANGE, "test", true, null, ("this is addConfirmListener test!====" + i).getBytes());
            Thread.sleep(1);
//                channel.waitForConfirms(0); 单条等待结果会阻塞channel.waitForConfirms();，channel.waitForConfirms(Long l);
        }
//            channel.waitForConfirmsOrDie();  批量等待結果，会阻塞channel.waitForConfirmsOrDie(),channel.waitForConfirmsOrDie(Long l)
        /**
         * 测试退回信息
         * 第三个参数为：mandatory true代表如果消息无法正常投递则return回生产者，如果false，则直接将消息放弃。
         * 此条消息Broker是会通过监听器confirm反馈为接收状态，但是由于没有找到队列会被退回信息
         */
        channel.basicPublish(Producer.LISTENEREXCHANGE, "23413243456234", true, null,"this is ReturnListener test!".getBytes());
        Thread.sleep(100000);
//        }
    }
}
