package com.lvlivejp.gulimall;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@EnableRabbit
@SpringBootApplication
public class GulimallApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallApplication.class, args);
    }

}
