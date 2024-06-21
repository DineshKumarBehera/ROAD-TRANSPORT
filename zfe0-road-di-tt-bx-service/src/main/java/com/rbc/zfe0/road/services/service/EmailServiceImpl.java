package com.rbc.zfe0.road.services.service;

import com.rbc.zfe0.road.services.dto.email.EmailRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


@Service
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Configuration
public class EmailServiceImpl implements EmailService{

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendEmail(EmailRequest emailRequest) throws MessagingException {
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);

        String[] sendTO = emailRequest.getSendTo().split(";");
        String sub = emailRequest.getSubject();
        String content = emailRequest.getContent();
        boolean html = emailRequest.isHtmlFormat();
        String sendFrom = emailRequest.getSendFrom();
        helper.setFrom(sendFrom);
        helper.setTo(sendTO);
        helper.setSubject(sub);
        helper.setText(content,html);
        if(emailRequest.isAttachment())
            for(MultipartFile file :emailRequest.getFiles())
                helper.addAttachment(file.getOriginalFilename(),file);
        javaMailSender.send(msg);

    }

}
