package com.darshan.urlshortener.config;

import com.darshan.urlshortener.entity.ShortUrl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, ShortUrl>
    redisTemplate(
            RedisConnectionFactory connectionFactory) {

        RedisTemplate<String, ShortUrl>
                template =
                new RedisTemplate<>();

        template.setConnectionFactory(
                connectionFactory);

        return template;
    }
}