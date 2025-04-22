package com.example.user_service.config;

import com.example.user_service.service.EmailService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestEmailConfig {

    @Bean
    @Primary
    public EmailService emailService() {
        return mock(EmailService.class);
    }
}
