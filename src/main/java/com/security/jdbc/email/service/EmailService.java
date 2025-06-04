package com.security.jdbc.email.service;

import com.security.jdbc.email.dto.EmailPayloads;

public interface EmailService {
    String SendEmailText(EmailPayloads emailPayloads);
}
