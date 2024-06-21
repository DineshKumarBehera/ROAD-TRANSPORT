package com.rbc.zfe0.road.services.controller;

import com.rbc.zfe0.road.services.dto.email.EmailRequest;
import com.rbc.zfe0.road.services.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/v1/email")
public class EmailController {

    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping(value = "/sendEmail", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public void sendEmail(@RequestBody @ModelAttribute EmailRequest emailRequest) {
        log.info("Send Email");
        try{
            emailService.sendEmail(emailRequest);
            log.info("Email sent success.");
        } catch(Exception e) {
            log.error("Failed to send email : Exception {}", e.getMessage());
            e.printStackTrace();
        }

    }
}
