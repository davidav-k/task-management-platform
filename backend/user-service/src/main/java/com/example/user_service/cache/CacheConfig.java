package com.example.user_service.cache;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    @Bean(name = "userLoginCache")
    public CacheStore<String, Integer> cacheStore() {
        return new CacheStore<>(900, TimeUnit.SECONDS);
    }

}
