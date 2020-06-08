package com.lvlivejp.gulimall.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.text.MessageFormat;

@Configuration
@Slf4j
public class RabbitMqConfig {

    @Autowired
    public RabbitTemplate rabbitTemplate;

    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @PostConstruct //将在rabbitTemplate依赖注入完成后被自动调用
    public void initRabbitTemplate(){

        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                log.info(MessageFormat.format("消息发送ConfirmCallback:{0},{1},{2}", correlationData, ack, cause));
            }
        });

        //只有在exchange收到，但queue没收到的情况才会触发该CallBack
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) ->
                log.info(MessageFormat.format("消息发送ReturnCallback:{0},{1},{2},{3},{4}", message, replyCode, replyText, exchange, routingKey)));

    }
}
