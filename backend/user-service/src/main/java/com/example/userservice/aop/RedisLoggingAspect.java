package com.example.userservice.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
@Slf4j
public class RedisLoggingAspect {



    @Before("execution(* org.springframework.data.redis.core.RedisTemplate.*(..))")
    public void logBeforeRedisOperation() {
        log.info("Performing Redis operation");
    }

    @AfterReturning("execution(* org.springframework.data.redis.core.RedisTemplate.*(..))")
    public void logAfterRedisOperation() {
        log.info("Redis operation completed successfully");
    }

    @AfterThrowing(pointcut = "execution(* org.springframework.data.redis.core.RedisTemplate.*(..))", throwing = "ex")
    public void logRedisOperationError(Exception ex) {
        log.error("Redis operation failed", ex);
    }
}

