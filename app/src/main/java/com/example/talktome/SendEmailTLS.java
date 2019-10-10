package com.example.talktome;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendEmailTLS {
    private String username;
    private String password = "kosvkeonriszprry";

    public String sendEmail(String subject, String content, String receiver, String sender) {
        username = sender;

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            final Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(receiver)
            );
            message.setSubject(subject);
            message.setText(content);

            new Thread() {
                @Override
                public void run() {
                    try {
                        Transport.send(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();

            return "sent";
        } catch (MessagingException e) {
            e.printStackTrace();
            return e.getLocalizedMessage();
        }
    }
}