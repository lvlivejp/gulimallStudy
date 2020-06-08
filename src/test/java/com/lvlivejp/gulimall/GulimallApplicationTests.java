package com.lvlivejp.gulimall;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.redisson.RedissonLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
class GulimallApplicationTests {

    @Autowired
    StringRedisTemplate template;
    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private AmqpAdmin amqpAdmin;

    @SneakyThrows
    @Test
    void contextLoads() {
        template.opsForValue().set("aaa","222");
        RLock lock = redissonClient.getLock("111");
        lock.lock();
        Thread.sleep(60000L);
        lock.unlock();
    }

    @Test
    void rabbitMqAmqpAdmin() {
        amqpAdmin.declareExchange(new DirectExchange("hello-java-direct"));
        amqpAdmin.declareQueue(new Queue("hello-java-direct-queue"));
        //String destination, DestinationType destinationType, String exchange, String routingKey,
        //			@Nullable Map<String, Object> arguments
        amqpAdmin.declareBinding(new Binding("hello-java-direct-queue", Binding.DestinationType.QUEUE,"hello-java-direct","hello-java",null));
    }

}
