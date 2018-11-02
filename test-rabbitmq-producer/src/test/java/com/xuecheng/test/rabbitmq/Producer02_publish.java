package com.xuecheng.test.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;


import java.util.concurrent.TimeoutException;

/**
 * @Auther: 0
 * @Date: 2018/11/2 14:59
 * @Description: 发布订阅模式
 */

public class Producer02_publish {
    public static final String QUEUE_INFORM_EMAIL = "queue_inform_email";

    public static final String EXCHANGE_FANOUT_INFORM = "exchange_fanout_inform";

    public static final String QUEUE_INFORM_SMS = "queue_inform_sms";


    public static void main(String[] args) {

        Connection connection = null;

        Channel channel = null;


        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();

            connectionFactory.setHost("127.0.0.1");
            connectionFactory.setPort(5672);
            connectionFactory.setUsername("guest");
            connectionFactory.setPassword("guest");
            connectionFactory.setVirtualHost("/");


            connection = connectionFactory.newConnection();
            channel = connection.createChannel();

            channel.exchangeDeclare(EXCHANGE_FANOUT_INFORM, BuiltinExchangeType.FANOUT);

            channel.queueDeclare(QUEUE_INFORM_SMS, true, false, false, null);
            channel.queueDeclare(QUEUE_INFORM_EMAIL, true, false, false, null);

            channel.queueBind(QUEUE_INFORM_EMAIL, EXCHANGE_FANOUT_INFORM, "");
            channel.queueBind(QUEUE_INFORM_SMS, EXCHANGE_FANOUT_INFORM, "");

            for (int i = 0; i < 10; i++) {
                String message = "send message " + System.currentTimeMillis();
                channel.basicPublish(EXCHANGE_FANOUT_INFORM, "", null, message.getBytes());

                System.out.println("send message.." + message);

            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {

            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

}
