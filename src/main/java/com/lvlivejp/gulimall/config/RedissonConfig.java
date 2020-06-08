package com.lvlivejp.gulimall.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
public class RedissonConfig {
    @Bean
    public RedissonClient redissonClient(RedisProperties redisProperties){
        Config config = new Config();
        SingleServerConfig serverConfig = config.useSingleServer()
                .setAddress("redis://" + redisProperties.getHost()+":"+redisProperties.getPort());

        if(!StringUtils.isEmpty(redisProperties.getPassword())) {
            serverConfig.setPassword(redisProperties.getPassword());
        }
        serverConfig.setDatabase(redisProperties.getDatabase());
        return Redisson.create(config);
    }
}
