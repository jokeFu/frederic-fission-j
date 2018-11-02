package com.xuecheng.test.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Auther: 0
 * @Date: 2018/11/2 16:46
 * @Description:
 */

public class Producer04_topics {

    private static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
    private static final String ROUTINGKEY_EMAIL = "inform.#.email.#";


    private static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    private static final String ROUTINGKEY_SMS = "inform.#.sms.#";

    private static final String EXCHANGE_TOPICS_INFORM = "exchange_topics_infrom";


    public static void main(String[] args) {
        Connection connection = null;
        Channel channel = null;

        ConnectionFactory connectionFactory = new ConnectionFactory();

        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");

        try {
            connection = connectionFactory.newConnection();
            channel = connection.createChannel();

            //声明一个交换机
            channel.exchangeDeclare(EXCHANGE_TOPICS_INFORM, BuiltinExchangeType.TOPIC);

            //声明一个队列
            channel.queueDeclare(QUEUE_INFORM_SMS, true, false, false, null);
            channel.queueDeclare(QUEUE_INFORM_EMAIL, true, false, false, null);

            //将队列绑定到交换机，目的就是让交换机将消息转换到队列

            channel.queueBind(QUEUE_INFORM_EMAIL, EXCHANGE_TOPICS_INFORM, ROUTINGKEY_EMAIL);
            channel.queueBind(QUEUE_INFORM_SMS, EXCHANGE_TOPICS_INFORM, ROUTINGKEY_SMS);

            for (int i = 0; i < 3; i++) {
                String message = "send sms and email message :" + System.currentTimeMillis();
                channel.basicPublish(EXCHANGE_TOPICS_INFORM, "inform.sms.email", null, message.getBytes());

                System.out.println("send message.." + message);


            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            try {
                channel.close();
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }


    }


}
