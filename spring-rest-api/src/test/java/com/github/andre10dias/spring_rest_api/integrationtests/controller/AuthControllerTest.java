package com.github.andre10dias.spring_rest_api.integrationtests.controller;

import com.github.andre10dias.spring_rest_api.config.TestConfig;
import com.github.andre10dias.spring_rest_api.integrationtests.testcontainers.AbstractIntegrationTest;
import com.github.andre10dias.spring_rest_api.integrationtests.testcontainers.dto.security.AccountCredentialsDTO;
import com.github.andre10dias.spring_rest_api.integrationtests.testcontainers.dto.security.TokenDTO;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerTest extends AbstractIntegrationTest {

    private static TokenDTO tokenDto;

    @BeforeAll
    static void setUp() {
        tokenDto = new TokenDTO();
    }

    @Test
    @Order(1)
    void signIn() {
        AccountCredentialsDTO credentialsDto =
                new AccountCredentialsDTO("leandro", "admin123");
        tokenDto = given()
                .basePath("/api/auth/v1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .port(TestConfig.SERVER_PORT)
                .body(credentialsDto)
                .when()
                .post("/signin")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(TokenDTO.class);

        assertNotNull(tokenDto.getAccessToken());
        assertNotNull(tokenDto.getRefreshToken());
    }

    @Test
    @Order(2)
    void refreshToken() {
        tokenDto = given()
                .basePath("/api/auth/v1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .port(TestConfig.SERVER_PORT)
                .header(
                        TestConfig.AUTHORIZATION_HEADER,
                        TestConfig.BEARER_TOKEN_PREFIX
                                + tokenDto.getRefreshToken()
                )
                .when()
                .put("/refresh")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(TokenDTO.class);

        assertNotNull(tokenDto.getAccessToken());
        assertNotNull(tokenDto.getRefreshToken());
    }

    @Test
    @Order(0)
    void testLoginWithInvalidPassword() {
        AccountCredentialsDTO credentialsDto =
                new AccountCredentialsDTO("leandro", "senhaErrada");

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .port(TestConfig.SERVER_PORT)
                .basePath("/api/person/v1")
                .body(credentialsDto)
                .when()
                .post("/signin")
                .then()
                .statusCode(403);
    }


}