package com.rbc.zfe0.road.eod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableJms
public class Zfe0RoadEodApplication {

    @Autowired
    JmsTemplate jmsTemplate;
    public static void main(String[] args) {
        SpringApplication.run(Zfe0RoadEodApplication.class, args);
    }

}
