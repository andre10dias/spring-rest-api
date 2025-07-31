package com.github.andre10dias.spring_rest_api.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.andre10dias.spring_rest_api.data.dto.security.TokenDTO;
import com.github.andre10dias.spring_rest_api.exception.InvalidCredentialsException;
import com.github.andre10dias.spring_rest_api.exception.InvalidJwtAuthenticationException;
import com.github.andre10dias.spring_rest_api.service.CustomUserDetailsService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
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

    private static final String BEARER = "Bearer ";
    private static final String ROLES = "roles";

    private final CustomUserDetailsService userDetailsService;

    Algorithm algorithm;

    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes(StandardCharsets.UTF_8));
        algorithm = Algorithm.HMAC256(secret);
    }

    public TokenDTO createAccessToken(String username, List<String> roles) {
        Instant now = Instant.now();
        Instant validity = now.plusMillis(expiration); // Expiração em milisegundos.
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

    public TokenDTO refreshToken(String refreshToken) {
        try {
            refreshToken = removeBearerPrefix(refreshToken);

            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(refreshToken);

            String type = decodedJWT.getClaim("type").asString();
            if (!"refresh".equals(type))
                throw new InvalidCredentialsException("Token is not a refresh token.");

            String username = decodedJWT.getSubject();
            List<String> roles = decodedJWT.getClaim(ROLES).asList(String.class);

            return createAccessToken(username, roles);
        } catch (JWTVerificationException ex) {
            throw new InvalidCredentialsException("Invalid or expired refresh token.");
        }
    }

    private String getAccessToken(String username, List<String> roles, Instant now, Instant validity) {
        // Retorna a URL da aplicação de onde o token será criado
        String issuerUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(Instant.from(now))
                .withExpiresAt(Instant.from(validity))
                .withIssuer(issuerUrl)
                .withClaim(ROLES, roles)
                .sign(algorithm);
    }
    
    private String getRefreshToken(String username, List<String> roles, Instant now) {
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(Instant.from(now))
                .withExpiresAt(now.plusMillis(expiration))
                .withClaim(ROLES, roles)
                .withClaim("type", "refresh")
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
        return removeBearerPrefix(bearerToken);
    }

    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            throw new InvalidJwtAuthenticationException("Token is missing or empty.");
        }

        try {
            DecodedJWT decodedJWT = decodedToken(token);
            if (decodedJWT.getExpiresAt().before(Date.from(Instant.now())))
                throw new TokenExpiredException("Token has expired.", null);

            return true;

        } catch (TokenExpiredException ex) {
            throw new InvalidJwtAuthenticationException("Token expired. Please login again.");

        } catch (SignatureVerificationException ex) {
            throw new InvalidJwtAuthenticationException("Invalid token signature.");

        } catch (AlgorithmMismatchException ex) {
            throw new InvalidJwtAuthenticationException("Token algorithm does not match expected.");

        } catch (JWTVerificationException ex) {
            // pega qualquer outro erro relacionado a verificação
            throw new InvalidJwtAuthenticationException("Invalid JWT token: " + ex.getMessage());

        } catch (Exception ex) {
            throw new InvalidJwtAuthenticationException("An unexpected error occurred while validating token.");
        }
    }

    private DecodedJWT decodedToken(String token) {
        JWTVerifier verifier = JWT.require(algorithm).build(); // usa a instância já criada
        return verifier.verify(token);
    }

    private String removeBearerPrefix(String token) {
        return token != null && token.startsWith(BEARER)
                ? token.substring(BEARER.length())
                : token;
    }

}
