package top.mengchao.rabbitmq;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Common {
    private static ConnectionFactory connectionFactory = new ConnectionFactory();
    public final static String IP = "127.0.0.1";
    public final static String USER="mengchaob";
    public final static String PASSWORD="top.mengchao.www";
    public final static String VIRTUALHOST = "mengchaobhost";
    public final static int port = 15672;
    /**
     * hellow模式
     */
    public final static String QUEUE_NAME_HELLOW = "hellowtest";
    /**
     * work模式
     */
    public final static String QUEUE_NAME_WORK = "rabbitmq_work";
    /**
     * 订阅发布模式，交换机
     *
     *  DIRECT("direct"),
     *     FANOUT("fanout"),
     *     TOPIC("direct"),
     *     HEADERS("headers");
     */
    public final static String QUEUE_NAME_DIRECT_EXCHANGE = "mengchaob.direct";
    public final static String QUEUE_NAME_FANOUT_EXCHANGE = "mengchaob.fanout";
    public final static String QUEUE_NAME_TOPIC_EXCHANGE = "mengchaob.topic";

    public static Connection getConnection(){
        Connection conn = null;
        try {
            connectionFactory.setHost(Common.IP);
//            connectionFactory.setPort(Common.port);
            connectionFactory.setUsername(Common.USER);
            connectionFactory.setPassword(Common.PASSWORD);
            connectionFactory.setVirtualHost(Common.VIRTUALHOST);
            conn = connectionFactory.newConnection();
            return conn;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
