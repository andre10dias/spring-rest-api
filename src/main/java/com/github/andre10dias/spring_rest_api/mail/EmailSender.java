package com.github.andre10dias.spring_rest_api.mail;

import com.github.andre10dias.spring_rest_api.config.EmailConfig;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@Component
public class EmailSender {

    private final JavaMailSender javaMailSender;
    private String to;
    private String subject;
    private String body;
    private List<InternetAddress> recipients;
    private File attachment;

    Logger logger = LoggerFactory.getLogger(EmailSender.class);

    public EmailSender(JavaMailSender javaMailSender) {
        this.recipients = new ArrayList<>();
        this.javaMailSender = javaMailSender;
    }

    public EmailSender to(String to) {
        this.to = to;
        try {
            this.recipients = getRecipients(to);
        } catch (AddressException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public EmailSender withSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public EmailSender withBody(String body) {
        this.body = body;
        return this;
    }

    public EmailSender attach(String fileDir) {
        this.attachment = new File(fileDir);
        return this;
    }

    public void send(EmailConfig config) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(config.getUsername());
            helper.setTo(recipients.toArray(new InternetAddress[0]));
            helper.setSubject(subject);
            helper.setText(body, true);
            if (attachment != null) {
                helper.addAttachment(attachment.getName(), attachment);
            }
            javaMailSender.send(message);
            logger.info("Email sent successfully");
            reset();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private void reset() {
        this.to = null;
        this.subject = null;
        this.body = null;
        this.recipients = null;
        this.attachment = null;
    }

    private List<InternetAddress> getRecipients(String to) throws AddressException {
        String toWithoutSpaces = to.replace(" ", "");
        StringTokenizer tokenizer = new StringTokenizer(toWithoutSpaces, ";");
        List<InternetAddress> recipientsList = new ArrayList<>();
        while (tokenizer.hasMoreTokens()) {
            String recipient = tokenizer.nextToken();
            recipientsList.add(new InternetAddress(recipient));
        }
        return recipientsList;
    }

}
