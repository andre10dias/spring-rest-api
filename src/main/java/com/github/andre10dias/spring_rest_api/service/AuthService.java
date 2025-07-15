package com.github.andre10dias.spring_rest_api.service;

import com.github.andre10dias.spring_rest_api.data.dto.security.AccountCredentialsDTO;
import com.github.andre10dias.spring_rest_api.data.dto.security.TokenDTO;
import com.github.andre10dias.spring_rest_api.exception.InvalidCredentialsException;
import com.github.andre10dias.spring_rest_api.repository.UserRepository;
import com.github.andre10dias.spring_rest_api.security.jwt.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Transactional
    public ResponseEntity<TokenDTO> signIn(AccountCredentialsDTO credentials) {
        var user = userRepository.findByUsername(credentials.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getUsername(), credentials.getPassword()
                    )
            );
        } catch (BadCredentialsException ex) {
            throw new InvalidCredentialsException("Invalid username or password.");
        }

        var token = jwtTokenProvider.createAccessToken(
                credentials.getUsername(), user.getRoles()
        );
        return ResponseEntity.ok(token);
    }

}
