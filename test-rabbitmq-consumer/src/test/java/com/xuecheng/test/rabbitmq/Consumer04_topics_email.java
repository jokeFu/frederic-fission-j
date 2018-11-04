package com.xuecheng.test.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;

import java.util.concurrent.TimeoutException;

public class Consumer04_topics_email {

    //email 的信息 大体的步骤是这样的  首先要声明一个交换机  然后声明一个队列  最后把队列绑定到交换机上

    //声明一个队列的名字
    private static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
    //routing key
    private static final String ROUTINGKEY_EMAIL = "inform.#.email.#";
    //交换机
    private static final String EXCHANGE_TOPICS_INFORM = "exchange_topics_inform";


    public static void main(String[] args) {
        Connection connection = null;

        Channel channel = null;


        ConnectionFactory factory = new ConnectionFactory();


        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setVirtualHost("/");

        try {
            connection = factory.newConnection();

            channel = connection.createChannel();

            //声明一个交换机
            channel.exchangeDeclare(EXCHANGE_TOPICS_INFORM, BuiltinExchangeType.TOPIC);
            //声明一个队列
            channel.queueDeclare(QUEUE_INFORM_EMAIL, true, false, false, null);
            //队列绑定首先是队列 然后是交换机 最后是routingkey
            channel.queueBind(QUEUE_INFORM_EMAIL, EXCHANGE_TOPICS_INFORM, ROUTINGKEY_EMAIL);

            DefaultConsumer consumer = new DefaultConsumer(channel) {

                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                    String exchange = envelope.getExchange();//交换
                    long deliveryTag = envelope.getDeliveryTag();//消息id
                    String routingKey  = envelope.getRoutingKey();//路由key

                    String message = new String(body, "utf-8");
                    System.out.println(message);

                }
            };

            channel.basicConsume(QUEUE_INFORM_EMAIL, true, consumer);





        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }


    }






}
