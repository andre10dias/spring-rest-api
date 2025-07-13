package com.github.andre10dias.spring_rest_api.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.email")
public class EmailConfig {

    private String host;
    private String port;
    private String username;
    private String password;
    private String from;
    private boolean ssl;

}
