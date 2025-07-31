package com.github.andre10dias.spring_rest_api.integrationtests.testcontainers.dto.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountCredentialsDTO {

    private String username;
    private String password;

}
