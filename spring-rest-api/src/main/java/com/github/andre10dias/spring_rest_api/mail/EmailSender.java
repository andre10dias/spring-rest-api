package com.github.andre10dias.spring_rest_api.mail;

import com.github.andre10dias.spring_rest_api.config.EmailConfig;
import com.github.andre10dias.spring_rest_api.exception.EmailSendingException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class EmailSender {

    private static final Logger logger = LoggerFactory.getLogger(EmailSender.class);
    private final JavaMailSender javaMailSender;

    private String to;
    private String subject;
    private String body;
    private List<InternetAddress> recipients;
    private File attachment;

    public EmailSender(JavaMailSender javaMailSender) {
        this.recipients = new ArrayList<>();
        this.javaMailSender = javaMailSender;
    }

    public EmailSender to(String to) {
        this.to = to;
        try {
            this.recipients = parseRecipients(to);
        } catch (AddressException e) {
            throw new EmailSendingException("Erro ao processar destinatários do e-mail.", e);
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
            logger.info("E-mail enviado com sucesso para {}", to);

        } catch (MessagingException e) {
            throw new EmailSendingException("Erro ao enviar e-mail", e);
        } finally {
            reset(); // limpa estado após envio
        }
    }

    private void reset() {
        this.to = null;
        this.subject = null;
        this.body = null;
        this.recipients = null;
        this.attachment = null;
    }

    private List<InternetAddress> parseRecipients(String to) throws AddressException {
        String sanitized = to.replace(" ", "");
        StringTokenizer tokenizer = new StringTokenizer(sanitized, ";");
        List<InternetAddress> result = new ArrayList<>();

        while (tokenizer.hasMoreTokens()) {
            result.add(new InternetAddress(tokenizer.nextToken()));
        }
        return result;
    }
}