package com.github.andre10dias.spring_rest_api.controller;

import com.github.andre10dias.spring_rest_api.controller.docs.EmailControllerDocs;
import com.github.andre10dias.spring_rest_api.data.dto.request.EmailRequestDTO;
import com.github.andre10dias.spring_rest_api.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/email/v1")
public class EmailController implements EmailControllerDocs {

    private final EmailService emailService;

    @PostMapping
    @Override
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequestDTO dto) {
        emailService.sendSimpleEmail(dto);
        return ResponseEntity.ok("E-mail enviado com sucesso.");
    }

    @PostMapping(value = "/with-attachment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Override
    public ResponseEntity<String> sendEmailWithAttachment(@RequestParam String emailRequestJson,
                                                          @RequestParam MultipartFile multipartFile) {
        emailService.sendEmailWithAttachment(emailRequestJson, multipartFile);
        return ResponseEntity.ok("E-mail com anexo enviado com sucesso.");
    }
}
