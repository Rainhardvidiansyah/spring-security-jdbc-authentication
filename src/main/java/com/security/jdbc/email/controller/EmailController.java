package com.security.jdbc.email.controller;


import com.security.jdbc.email.dto.EmailPayloads;
import com.security.jdbc.email.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/email")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }


    @PostMapping("/")
    public ResponseEntity<?> sendEmail(@RequestBody EmailPayloads emailPayloads){
        this.emailService.SendEmailText(emailPayloads);

        return new ResponseEntity<>("Email sent to: " + emailPayloads.getRecipient(), HttpStatus.OK);
    }
}
