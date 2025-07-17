package com.github.andre10dias.spring_rest_api.config;

public interface TestConfig {
    int SERVER_PORT = 8888;
    String AUTHORIZATION_HEADER = "Authorization";
    String ORIGIN_HEADER = "Origin";
    String REACT_ORIGIN_VALUE = "http://localhost:3000";
    String ANGULAR_ORIGIN_VALUE = "http://localhost:4200";
    String WRONG_ORIGIN_VALUE = "http://localhost:3001";
    String LOCAL_ORIGIN_VALUE = "http://localhost:8080";

    String BEARER_TOKEN_PREFIX = "Bearer ";
}
