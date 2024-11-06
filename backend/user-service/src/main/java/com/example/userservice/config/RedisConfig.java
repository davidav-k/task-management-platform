package com.example.userservice.config;

import com.example.userservice.entity.RefreshToken;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.RedisKeyValueTemplate;
import org.springframework.data.redis.core.convert.KeyspaceConfiguration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import java.time.Duration;
import java.util.Collections;

@Configuration
@EnableRedisRepositories(enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP)
public class RedisConfig {

    @Value("${app.jwt.refreshTokenExpiration}")
    private Duration refreshTokenExpiration;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory(RedisProperties redisProperties) {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(redisProperties.getHost());
        configuration.setPort(redisProperties.getPort());
        return new LettuceConnectionFactory(configuration);
    }


//    @Bean
//    public JedisConnectionFactory jedisConnectionFactory(RedisProperties redisProperties){
//        RedisStandaloneConfiguration configuration= new RedisStandaloneConfiguration();
//
//        configuration.setHostName(redisProperties.getHost());
//        configuration.setPort(redisProperties.getPort());
//
//        return new JedisConnectionFactory(configuration);
//    }
//
//    public class RRefreshTokenKeyspaceConfiguration extends KeyspaceConfiguration{
//
//        private static final String REFRESH_TOKEN_KEYSPACE = "refresh_tokens";
//
//        @Override
//        protected @NotNull Iterable<KeyspaceSettings> initialConfiguration() {
//            KeyspaceSettings keyspaceSettings = new KeyspaceSettings(RefreshToken.class, REFRESH_TOKEN_KEYSPACE);
//            keyspaceSettings.setTimeToLive(refreshTokenExpiration.getSeconds());
//
//            return Collections.singleton(keyspaceSettings);
//        }
//    }




}
