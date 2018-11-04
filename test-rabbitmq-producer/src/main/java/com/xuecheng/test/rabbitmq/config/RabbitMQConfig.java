package com.xuecheng.test.rabbitmq.config;



import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by 0 on 2018/11/2
 */
@Configuration
public class RabbitMQConfig {

    //email消息队列
    public static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
    public static final String ROUTINGKEY_EMAIL = "inform.#.email.#";

    public static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    public static final String ROUTINGKEY_SMS = "inform.#.sms.#";

    public static final String EXCHANGE_TOPICS_INFORM = "exchange_topics_inform";

    @Bean(EXCHANGE_TOPICS_INFORM)
    public Exchange EXCHANGE_TOPICS_INFORM() {
        //声明交换机
        //指定交换机的名称
        return ExchangeBuilder.topicExchange(EXCHANGE_TOPICS_INFORM).durable(true).build();
    }

    //声明队列
    @Bean(QUEUE_INFORM_EMAIL)
    public Queue QUEUE_INFORM_EMAIL(){

        return new Queue(QUEUE_INFORM_EMAIL);
    }
    //声明队列
    @Bean(QUEUE_INFORM_SMS)
    public Queue QUEUE_INFORM_SMS(){
        return new Queue(QUEUE_INFORM_SMS);
    }

    //绑定队列
    @Bean
    public Binding BINDING_QUEUE_INFORM_EMAIL(@Qualifier(QUEUE_INFORM_EMAIL) Queue queue, @Qualifier(EXCHANGE_TOPICS_INFORM) Exchange exchange) {

        return BindingBuilder.bind(queue).to(exchange).with(ROUTINGKEY_EMAIL).noargs();

    }

    @Bean
    public Binding BINDING_QUEUE_INFORM_SMS(@Qualifier(QUEUE_INFORM_SMS) Queue queue, @Qualifier(EXCHANGE_TOPICS_INFORM) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTINGKEY_SMS).noargs();

    }





}
