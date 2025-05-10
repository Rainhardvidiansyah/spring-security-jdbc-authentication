package com.security.jdbc.email.service;

import com.security.jdbc.email.dto.EmailPayloads;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    void SendEmailText() {
        String sender = "erpmodule.@email.com";

        EmailPayloads emailPayloads = new EmailPayloads();
        emailPayloads.setRecipient("user@email.com");
        emailPayloads.setSubject("Important message");
        emailPayloads.setMsgBody("This is body");

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(sender);
        simpleMailMessage.setTo(emailPayloads.getRecipient());
        simpleMailMessage.setSubject(emailPayloads.getSubject());
        simpleMailMessage.setText(emailPayloads.getMsgBody());

        javaMailSender.send(simpleMailMessage);

        String sendingEmail = emailService.SendEmailText(emailPayloads);

        Assertions.assertEquals("Successfully send email to: user@email.com", sendingEmail);


        Mockito.verify(javaMailSender, Mockito.times(1)).send(simpleMailMessage);
    }


    @Test
    void testError_thenReturnExpectedErrorMessage(){
        // Arrange
        String sender = "module.rainhard@email.com";
        EmailPayloads emailPayloads = new EmailPayloads();
        emailPayloads.setRecipient("fail@example.com");
        emailPayloads.setSubject("Fail Test");
        emailPayloads.setMsgBody("Will cause exception");

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(sender);
        simpleMailMessage.setTo(emailPayloads.getRecipient());
        simpleMailMessage.setSubject(emailPayloads.getSubject());
        simpleMailMessage.setText(emailPayloads.getMsgBody());

        Mockito.doThrow(new MailException("Failed to send email") {}).when(javaMailSender).send(simpleMailMessage);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                emailService.SendEmailText(emailPayloads)
        );


        Assertions.assertEquals("Failed to send email", exception.getMessage());

        Mockito.verify(javaMailSender, Mockito.times(1)).send(simpleMailMessage);
    }
}