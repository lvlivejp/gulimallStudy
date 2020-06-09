package com.lvlivejp.gulimall.service;

import com.alibaba.fastjson.JSONObject;
import com.lvlivejp.gulimall.vo.OrderVo;
import com.lvlivejp.gulimall.vo.ProductVo;
import com.lvlivejp.gulimall.vo.UserVo;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RabbitListener(queues = "delay-order-release-queue")
public class RabbitMqOrderConsumerService {

    //根据函数的入参自动匹配相应的方法
    @SneakyThrows
    @RabbitHandler
    public void consumerMessageUserVo(Message message, OrderVo orderVo, Channel channel){
        log.info("接收到订单释放消息：" + JSONObject.toJSONString(orderVo));
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        // long deliveryTag 信道内递增的ID号, boolean multiple 是否批量确认
        channel.basicAck(deliveryTag,false);
    }

}
