package com.github.andre10dias.spring_rest_api.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.andre10dias.spring_rest_api.data.dto.security.TokenDTO;
import com.github.andre10dias.spring_rest_api.exception.InvalidJwtAuthenticationException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class JwtTokenProvider {

    @Value("${security.jwt.token.secret-key}")
    private String secret;

    @Value("${security.jwt.token.expire-length}")
    private long expiration;

    private final UserDetailsService userDetailsService;

    Algorithm algorithm;

    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes(StandardCharsets.UTF_8));
        algorithm = Algorithm.HMAC256(secret);
    }

    public TokenDTO createAccessToken(String username, List<String> roles) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime validity = now.plusMinutes(expiration);
        String accessToken = getAccessToken(username, roles, now, validity);
        String refreshToken = getRefreshToken(username, roles, now);
        return new TokenDTO(
                username,
                true,
                now,
                validity,
                accessToken,
                refreshToken
        );
    }
    
    private String getAccessToken(String username, List<String> roles, LocalDateTime now, LocalDateTime validity) {
        // Retorna a URL da aplicação de onde o token será criado
        String issuerUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(Instant.from(now))
                .withExpiresAt(Instant.from(validity))
                .withIssuer(issuerUrl)
                .withClaim("roles", roles)
                .sign(algorithm);
    }
    
    private String getRefreshToken(String username, List<String> roles, LocalDateTime now) {
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(Instant.from(now))
                .withExpiresAt(Instant.from(now.plusMinutes(expiration * 3)))
                .withClaim("roles", roles)
                .sign(algorithm);
    }

    public Authentication getAuthentication(String token) {
        DecodedJWT decodedJWT = decodedToken(token);
        String username = decodedJWT.getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring("Bearer ".length());
        }
        return null;
    }

    public boolean validateToken(String token) {
        String message = "Expired or invalid Token";
        try {
            if (token == null || token.isEmpty()) {
                throw new InvalidJwtAuthenticationException(message);
            }
            DecodedJWT decodedJWT = decodedToken(token);
            return !decodedJWT.getExpiresAt().before(Date.from(Instant.now()));
        } catch (Exception e) {
            throw new InvalidJwtAuthenticationException(message);
        }
    }

    private DecodedJWT decodedToken(String token) {
        JWTVerifier verifier = JWT.require(algorithm).build(); // usa a instância já criada
        return verifier.verify(token);
    }

}
