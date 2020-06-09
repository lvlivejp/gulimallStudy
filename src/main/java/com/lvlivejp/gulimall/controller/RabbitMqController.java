package com.lvlivejp.gulimall.controller;

import com.lvlivejp.gulimall.vo.OrderVo;
import com.lvlivejp.gulimall.vo.ProductVo;
import com.lvlivejp.gulimall.vo.UserVo;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("mq")
public class RabbitMqController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/send")
    public String send(){
        for (int i = 0; i < 10; i++) {
            CorrelationData correlationData = new CorrelationData();
            correlationData.setId(i+"");
            if(i%2==0){
                UserVo userVo = new UserVo();
                userVo.setUserId(i);
                userVo.setUserName("张三");
                rabbitTemplate.convertAndSend("hello-java-direct1","hello-java",userVo,correlationData);
            }else{
                ProductVo productVo= new ProductVo();
                productVo.setProductId(i);
                productVo.setProductName("手机");
                rabbitTemplate.convertAndSend("hello-java-direct","hello-java1",productVo,correlationData);
            }
        }
        return "OK";
    }

    @GetMapping("/order")
    public String order(){
        for (int i = 0; i < 10; i++) {
            CorrelationData correlationData = new CorrelationData();
            correlationData.setId(i+"");
            OrderVo orderVo = new OrderVo();
            orderVo.setOrderId(i);
            orderVo.setMemo("华为");
            rabbitTemplate.convertAndSend("delay-order-exchange","order.order",orderVo,correlationData);
        }
        return "OK";
    }
}
