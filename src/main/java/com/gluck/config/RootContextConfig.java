package com.gluck.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.Jedis;

@Configuration
@ComponentScan({ "com.gluck.dao, com.gluck.service" })
public class RootContextConfig {

    @Bean
    public Jedis jedis() {
        return new Jedis("localhost");
    }

}