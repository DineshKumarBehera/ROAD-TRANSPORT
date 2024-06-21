package com.rbc.zfe0.road.services.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        // Set your mail server configuration
        mailSender.setHost("your.mail.server.host");
        mailSender.setPort(587);
        mailSender.setUsername("your.username");
        mailSender.setPassword("your.password");
        return mailSender;
    }
}

