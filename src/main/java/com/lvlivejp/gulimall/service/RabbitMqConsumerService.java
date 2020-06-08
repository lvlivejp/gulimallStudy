package com.lvlivejp.gulimall.service;

import com.alibaba.fastjson.JSONObject;
import com.lvlivejp.gulimall.vo.ProductVo;
import com.lvlivejp.gulimall.vo.UserVo;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.impl.AMQImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RabbitListener(queues = "hello-java-direct-queue")
public class RabbitMqConsumerService {

//    @RabbitListener(queues = "hello-java-direct-queue")
//    public void consumerMessage(Message message){
//        log.info("通过Message方式获取消息：" + JSONObject.toJSONString(message));
//    }

    //根据函数的入参自动匹配相应的方法
    @SneakyThrows
    @RabbitHandler
    public void consumerMessageUserVo(Message message,UserVo userVo, Channel channel){
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        // long deliveryTag 信道内递增的ID号, boolean multiple 是否批量确认
        channel.basicAck(deliveryTag,false);
        log.info("通过指定UserVo的方式获取消息：" + JSONObject.toJSONString(userVo));

    }

    //根据函数的入参自动匹配相应的方法
    @SneakyThrows
    @RabbitHandler
    public void consumerMessageProductVo(Message message,ProductVo productVo, Channel channel){
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        // long deliveryTag 信道内递增的ID号, boolean multiple 是否批量确认, boolean requeue 是否重新入队
        channel.basicNack(deliveryTag,false,false);
        log.info("通过指定ProductVo的方式获取消息：" + JSONObject.toJSONString(productVo));
    }
}
