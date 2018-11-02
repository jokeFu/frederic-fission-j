package com.xuecheng.test.rabbitmq;

import ch.qos.logback.classic.turbo.TurboFilter;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Auther: 0
 * @Date: 2018/11/2 13:47
 * @Description:
 */

public class Consumer01 {

    public static final String QUEUE = "helloworld";



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
            connection=  connectionFactory.newConnection();
            channel = connection.createChannel();

            channel.queueDeclare(QUEUE, true, false, false, null);


            DefaultConsumer consumer = new DefaultConsumer(channel){

                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String exchange = envelope.getExchange();
                    long deliveryTag = envelope.getDeliveryTag();
                    String routingKey = envelope.getRoutingKey();
                    String s = new String(body, "utf-8");
                    System.out.println(exchange+"----------"+deliveryTag+"=========="+routingKey+"----------"+s);



                }
            };

            channel.basicConsume(QUEUE, true, consumer);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }


    }


}
