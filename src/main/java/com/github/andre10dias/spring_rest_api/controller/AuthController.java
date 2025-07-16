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
import org.springframework.web.bind.annotation.*;

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
        TokenDTO token = authService.signIn(credentials);
        return ResponseEntity.ok(token);
    }

    @PutMapping("/refresh")
    @Override
    public ResponseEntity<TokenDTO> refreshToken(@RequestHeader("Authorization") String refreshToken) {
        if (!StringUtils.hasText(refreshToken))
            throw new InvalidCredentialsException("Refresh token must not be empty.");
        TokenDTO token = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(token);
    }

    private boolean credentialsValid(AccountCredentialsDTO credentials) {
        return credentials != null
                && StringUtils.hasText(credentials.getUsername())
                && StringUtils.hasText(credentials.getPassword());
    }

}
