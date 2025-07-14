package com.github.andre10dias.spring_rest_api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.andre10dias.spring_rest_api.config.EmailConfig;
import com.github.andre10dias.spring_rest_api.data.dto.request.EmailRequestDTO;
import com.github.andre10dias.spring_rest_api.exception.EmailSendingException;
import com.github.andre10dias.spring_rest_api.mail.EmailSender;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final EmailConfig emailConfig;

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public void sendSimpleEmail(EmailRequestDTO dto) {
        send(dto, null);
    }

    public void sendEmailWithAttachment(String emailRequestJson, MultipartFile multipartFile) {
        File tempFile = null;

        try {
            EmailRequestDTO dto = new ObjectMapper().readValue(emailRequestJson, EmailRequestDTO.class);
            tempFile = File.createTempFile("attachment", multipartFile.getOriginalFilename());
            multipartFile.transferTo(tempFile);

            send(dto, tempFile);

        } catch (JsonProcessingException e) {
            String message = String.format("Erro ao converter JSON do e-mail: %s", e.getMessage());
            logger.error(message, e);
            throw new EmailSendingException(message, e);
        } catch (IOException e) {
            String message = String.format("Erro ao salvar anexo temporário: %s", e.getMessage());
            logger.error(message, e);
            throw new EmailSendingException(message, e);
        } finally {
            if (tempFile != null && tempFile.exists()) {
                try {
                    Files.delete(tempFile.toPath());
                } catch (IOException e) {
                    logger.warn("Falha ao excluir arquivo temporário: {}", tempFile.getAbsolutePath());
                }
            }
        }
    }

    private void send(EmailRequestDTO dto, File attachment) {
        EmailSender emailSender = new EmailSender(javaMailSender);

        emailSender
                .to(dto.getTo())
                .withSubject(dto.getSubject())
                .withBody(dto.getBody());

        if (attachment != null) {
            emailSender.attach(attachment.getAbsolutePath());
        }

        emailSender.send(emailConfig);
    }
}