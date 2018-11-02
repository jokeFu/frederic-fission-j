package com.xuecheng.test.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;


import java.util.concurrent.TimeoutException;

/**
 * @Auther: 0
 * @Date: 2018/11/2 11:37
 * @Description:
 */

public class Product01 {

    public static final String QUEUE = "helloworld";

    public static void main(String[] args) {
        //连接
        Connection connection = null;
        //通道
        Channel channel = null;
        //通过连接工厂连接

        try {
            //给mq发送信息
            //连接mq
            //通过连接工厂创建连接
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost("127.0.0.1");
            connectionFactory.setPort(5672);
            connectionFactory.setUsername("guest");
            connectionFactory.setPassword("guest");
            connectionFactory.setVirtualHost("/");
            connection = connectionFactory.newConnection();
            channel = connection.createChannel();

            //声明一个队列
            /**
             * 参数 String exchange ,String routingkey,BasticProperties props, byte [] body
             * 1、队列的名称
             * 2、是否持久化，true表示持久化，当mq重启之后此队列还在
             * 3、是否独占通道，
             * 4、true表示自动删除，mq重启后自动删除
             * 5、队列参数列表
             *
             */


            channel.queueDeclare(QUEUE, true, false, false, null);

            //发送消息
            /**
             *  参数String exchange String routingkey, BasicProperties props byte [] body
             *  1、exchange 交换机名称，不指定交换机设置为空字符串，mq会使用一个默认的交换机
             *  2、routingKey
             *  路由key，交换机跟路由key来转发消息，由于没有指定交换机，所以routingkey设置为队列的名称
             *  3、消息属性
             *  4、消息内容
             *
             */

            String message = " hello 小明" + System.currentTimeMillis();
            channel.basicPublish("", QUEUE, null, message.getBytes());

            System.out.println("send message.." + message);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }finally {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }

        }
    }

}
