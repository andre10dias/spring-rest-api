package com.github.andre10dias.spring_rest_api.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "file")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FileStorageConfig {

    private String uploadDir;

}
