package com.rbc.zfe0.road.services.service;

import com.rbc.zfe0.road.services.dto.email.EmailRequest;

import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
public interface EmailService {
    public void sendEmail(EmailRequest emailRequest) throws MessagingException, MessagingException;
}
