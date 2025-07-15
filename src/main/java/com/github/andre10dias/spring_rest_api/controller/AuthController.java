package com.github.andre10dias.spring_rest_api.controller;

import com.github.andre10dias.spring_rest_api.controller.docs.AuthControllerDocs;
import com.github.andre10dias.spring_rest_api.data.dto.security.AccountCredentialsDTO;
import com.github.andre10dias.spring_rest_api.data.dto.security.TokenDTO;
import com.github.andre10dias.spring_rest_api.exception.InvalidCredentialsException;
import com.github.andre10dias.spring_rest_api.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth/v1")
@Tag(name = "Authentication", description = "Authentication Endpoints")
public class AuthController implements AuthControllerDocs {

    private final AuthService authService;

    @PostMapping("/signin")
    @Override
    public ResponseEntity<TokenDTO> signIn(@RequestBody AccountCredentialsDTO credentials) {
        if (!credentialsValid(credentials)) throw new InvalidCredentialsException("Username and password must not be empty.");
        return authService.signIn(credentials);
    }

    private boolean credentialsValid(AccountCredentialsDTO credentials) {
        return credentials != null
                && StringUtils.hasText(credentials.getUsername())
                && StringUtils.hasText(credentials.getPassword());
    }

}
