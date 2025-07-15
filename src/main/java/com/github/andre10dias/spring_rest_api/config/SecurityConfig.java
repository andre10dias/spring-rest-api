package com.github.andre10dias.spring_rest_api.config;

import com.github.andre10dias.spring_rest_api.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import java.util.Map;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    PasswordEncoder passwordEncoder() {
        String pbkdf2String = "pbkdf2";
        Map<String, PasswordEncoder> encoders = new java.util.HashMap<>();
        Pbkdf2PasswordEncoder pbkdf2Encoder = new Pbkdf2PasswordEncoder(
                "",
                8,
                185000,
                Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256
        );
        encoders.put(pbkdf2String, pbkdf2Encoder);
        DelegatingPasswordEncoder encoder = new DelegatingPasswordEncoder(pbkdf2String, encoders);
        encoder.setDefaultPasswordEncoderForMatches(pbkdf2Encoder);
        return encoder;
    }

}
