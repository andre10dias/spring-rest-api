package com.github.andre10dias.spring_rest_api.integrationtests.testcontainers.dto.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TokenDTO {

    private String username;
    private Boolean authenticated;
    Instant created;
    Instant expiration;
    private String accessToken;
    private String refreshToken;

}
