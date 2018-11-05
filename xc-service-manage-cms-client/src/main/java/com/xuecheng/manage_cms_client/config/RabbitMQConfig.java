package com.xuecheng.manage_cms_client.config;


import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: 0
 * @Date: 2018/11/5 13:53
 * @Description:
 */
@Configuration
public class RabbitMQConfig {

    public static final String EX_CMS_POSTPAGE = "ex_cms_postpage";

    //从配置的文件注入队列名称
    @Value("${xuecheng.mq.queue}")
    private String queue_cms_postpage;

    //从配置文件注入routingkey
    @Value("${xuecheng.mq.routingkey}")
    private String routingkey;

    //声明交换机
    @Bean(EX_CMS_POSTPAGE)
    public Exchange EX_CMS_POSTPAGE() {
        return ExchangeBuilder.directExchange(EX_CMS_POSTPAGE).durable(true).build();
    }

    //声明队列，从配置文件注入队列的名称
    @Bean("${xuecheng.mq.queue}")
    public Queue QUEUE_CMS_POSTPAGE() {
        return new Queue(queue_cms_postpage);
    }

    //绑定队列到交换机
    public Binding BINDING_QUEUE_CMS_POSTPAGE(@Qualifier("${xuecheng.mq.queue}") Queue queue, @Qualifier(EX_CMS_POSTPAGE) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingkey).noargs();
    }




}
