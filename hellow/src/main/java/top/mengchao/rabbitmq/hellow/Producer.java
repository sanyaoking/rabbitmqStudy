package top.mengchao.rabbitmq.hellow;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import top.mengchao.rabbitmq.Common;

import java.util.Scanner;

public class Producer {
    public static void main(String[] argv) throws Exception {
        /**
         * 1. 创建connect
         * 2. 创建channel
         * 3. 绑定channel和队列
         * 4. 向队列发送数据
         */
        try (Connection connection = Common.getConnection();
             Channel channel = connection.createChannel()) {
            //创建队列,声明并创建一个队列，如果队列已存在，则使用这个队列
            //第一个参数：队列名称ID
            //第二个参数：是否持久化，false对应不持久化数据，MQ停掉数据就会丢失
            //第三个参数：是否队列私有化，false则代表所有消费者都可以访问，true代表只有第一次拥有它的消费者才能一直使用，其他消费者不让访问
            //第四个：是否自动删除,false代表连接停掉后不自动删除掉这个队列
            //其他额外的参数, null
            channel.queueDeclare(Common.QUEUE_NAME_HELLOW, false, false, false, null);
            String message = "Hello World!";
            Scanner input =new Scanner(System.in);
            boolean b = true;
            while(b) {
                //四个参数
                //exchange 交换机，暂时用不到，在后面进行发布订阅时才会用到
                //队列名称
                //额外的设置属性
                //最后一个参数是要传递的消息字节数组
                channel.basicPublish("", Common.QUEUE_NAME_HELLOW, null, message.getBytes());
                System.out.print("请输入内容：");
                message = input.next();
                if("exit".equals(message)){
                    b=false;
//                    channel.close();
//                    connection.close();
//                    因为在try（）中所以不需要自己关闭对应的资源
                }
            }
        }

    }
}
