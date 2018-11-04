package com.xuecheng.test.rabbitmq.mq;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.stereotype.Component;

@Component
public class ReceiveHandler {
    //监听email的信息，当接收到队列的消息的时候调用此方法

    @RabbitListener(queues = "queue_inform_email")
    public void send_email(String msg, Message message, Channel channel) {
        System.out.println(msg);
    }

    @RabbitListener(queues = "queue_inform_sms")
    public void send_sms(String msg, Message message, Channel channel) {
        System.out.println(msg);
    }


}
