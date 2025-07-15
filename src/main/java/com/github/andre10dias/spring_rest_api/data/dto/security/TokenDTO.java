package com.github.andre10dias.spring_rest_api.data.dto.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TokenDTO {

    private String username;
    private Boolean authenticated;
    LocalDateTime created;
    LocalDateTime expiration;
    private String accessToken;
    private String refreshToken;

}
