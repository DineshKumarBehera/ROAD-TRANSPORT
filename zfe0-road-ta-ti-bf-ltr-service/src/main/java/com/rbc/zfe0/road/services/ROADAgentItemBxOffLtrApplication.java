package com.rbc.zfe0.road.services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@PropertySource("classpath:labels.properties")
@EnableAsync
public class ROADAgentItemBxOffLtrApplication {
    public static void main(String[] args) {
        SpringApplication.run(ROADAgentItemBxOffLtrApplication.class, args);
    }
}

