package com.lvlivejp.gulimall.controller;

import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redis")
public class RedissonController {

    @Autowired
    private RedissonClient redissonClient;

    @GetMapping("/semaphore/tryAcquire")
    public String tryAcquire(){
        RSemaphore semaphoreTest = redissonClient.getSemaphore("semaphoreTest");
        semaphoreTest.trySetPermits(100);
        semaphoreTest.tryAcquire();
        return "OK";
    }

    @GetMapping("/semaphore/release")
    public String release(){
        RSemaphore semaphoreTest = redissonClient.getSemaphore("semaphoreTest");
        // 可以一直释放，并超过设置的信号量数值，和Java原生的Semaphore一致
        semaphoreTest.release();
        return "OK";
    }

}
