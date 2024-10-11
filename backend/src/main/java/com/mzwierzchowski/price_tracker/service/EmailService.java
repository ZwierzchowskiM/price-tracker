package com.mzwierzchowski.price_tracker.service;


import com.google.api.client.auth.oauth2.Credential;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.log4j.Log4j2;
import org.eclipse.angus.mail.smtp.SMTPTransport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.util.*;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.util.Map;


@Log4j2
@Service
public class EmailService {


    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;


    @Autowired
    private GoogleOAuth2Service googleOAuth2Service;


    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }


    public void sendPriceNotification(String to, String subject, Map<String, Object> model) throws Exception {
        String email = "pricetrakcer.alerts@gmail.com";
        Credential credential = googleOAuth2Service.getCredentials();

        if (!credential.refreshToken()) {
            throw new RuntimeException("Failed to refresh access token");
        }

        String accessToken = credential.getAccessToken();

        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.auth.mechanisms", "XOAUTH2");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.debug", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, accessToken);
            }
        });
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(email));
        message.setSubject(subject);
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

        message.setHeader("X-Entity-ID", UUID.randomUUID().toString());

        Context context = new Context();
        context.setVariables(model);
        String htmlContent = templateEngine.process("email-template", context);
        message.setContent(htmlContent, "text/html; charset=utf-8");

        Transport.send(message);
    }
}
