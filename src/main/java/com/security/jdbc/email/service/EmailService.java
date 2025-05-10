package com.security.jdbc.email.service;

import com.security.jdbc.email.dto.EmailPayloads;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService{

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    private static final String sender = "module.rainhard@email.com";


    public String SendEmailText(EmailPayloads emailPayloads) {
        LOGGER.info("SEND SIMPLE EMAIL METHOD IS HIT");

        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(sender);
            simpleMailMessage.setTo(emailPayloads.getRecipient());
            simpleMailMessage.setSubject(emailPayloads.getSubject());
            simpleMailMessage.setText(emailPayloads.getMsgBody());

            javaMailSender.send(simpleMailMessage);

            return "Successfully send email to: " + emailPayloads.getRecipient();

        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
