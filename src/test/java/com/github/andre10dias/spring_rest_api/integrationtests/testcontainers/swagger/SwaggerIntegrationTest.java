package com.github.andre10dias.spring_rest_api.integrationtests.testcontainers.swagger;

import com.github.andre10dias.spring_rest_api.config.TestConfig;
import com.github.andre10dias.spring_rest_api.integrationtests.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SwaggerIntegrationTest extends AbstractIntegrationTest {

    @Test
    void shouldOpenSwaggerUIPage() {
        var content = given()
                .basePath("/swagger-ui.html")
                .port(TestConfig.SERVER_PORT)
                .when().get().then()
                .statusCode(200)
                .extract()
                .body().asString();

        assertTrue(content.contains("Swagger UI"));
    }

}
