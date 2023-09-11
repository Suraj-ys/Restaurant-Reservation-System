package com.Restaurant.Reservation.System.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendSimpleEmail(String toEmail, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

    public void sendConfirmationEmailWithPdfAttachment(String toEmail, String subject, String text, byte[] pdfData, String pdfFileName) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            // Set the sender's email address
            helper.setFrom("surajbond28@gmail.com");

            // Set the recipient's email address
            helper.setTo(toEmail);

            // Set the email subject
            helper.setSubject(subject);

            // Set the email text
            helper.setText(text);

            // Attach the PDF file
            ByteArrayResource pdfAttachment = new ByteArrayResource(pdfData);
            helper.addAttachment(pdfFileName, pdfAttachment);

            // Send the email
            javaMailSender.send(message);
        } catch (MessagingException e) {
            // Handle exceptions
            e.printStackTrace();
        }
    }
}