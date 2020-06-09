package com.lvlivejp.gulimall.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class RabbitMqConfig {

    @Autowired
    public RabbitTemplate rabbitTemplate;

    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 死信队列，该队列无消费者监听，到达指定时间是将消息转发到下一个指定队列
     * 可以实现延时消息的效果
     * 业务场景：下单成功后，发送该消息，到达延迟时间后，判断订单是否支付
     * @return
     */
    @Bean
    public Queue orderQueue(){
        //String name, boolean durable, boolean exclusive, boolean autoDelete,
        //			@Nullable Map<String, Object> arguments
        Map<String, Object> arguments = new HashMap<>();
        //变成死信后将消息发送到哪个交换机
        arguments.put("x-dead-letter-exchange","delay-order-exchange");
        //消息多久超时，单位毫秒
        arguments.put("x-message-ttl",60000);
        //变成死信后发送消息的路由键
        arguments.put("x-dead-letter-routing-key","order.release");
        Queue queue = new Queue("delay-order-order-queue",true,false,false,arguments);
        return queue;
    }

    /**
     * 死信转发队列，需要消费机监听并处理
     * @return
     */
    @Bean
    public Queue orderReleaseQueue(){
        //String name, boolean durable, boolean exclusive, boolean autoDelete,
        //			@Nullable Map<String, Object> arguments
        Queue queue = new Queue("delay-order-release-queue",true,false,false,null);
        return queue;
    }

    /**
     * 死信交换机
     * @return
     */
    @Bean
    public Exchange orderExchange(){
        TopicExchange topicExchange = new TopicExchange("delay-order-exchange");
        return topicExchange;
    }

    /**
     * 下单成功后的binding
     * @return
     */
    @Bean
    public Binding orderOrderQueueBinding(){
        //String destination, DestinationType destinationType, String exchange, String routingKey,
        //			@Nullable Map<String, Object> arguments
        return new Binding("delay-order-order-queue", Binding.DestinationType.QUEUE,"delay-order-exchange",
                "order.order",null);

    }

    /**
     * 变成死信后的binding
     * 订单释放处理
     * @return
     */
    @Bean
    public Binding orderReleaseQueueBinding(){
        //String destination, DestinationType destinationType, String exchange, String routingKey,
        //			@Nullable Map<String, Object> arguments
        return new Binding("delay-order-release-queue", Binding.DestinationType.QUEUE,"delay-order-exchange",
                "order.release",null);

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
