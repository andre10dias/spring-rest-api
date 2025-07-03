package com.github.andre10dias.spring_rest_api.integrationtests.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.andre10dias.spring_rest_api.config.TestConfig;
import com.github.andre10dias.spring_rest_api.data.dto.v2.PersonDTOv2;
import com.github.andre10dias.spring_rest_api.integrationtests.testcontainers.AbstractIntegrationTest;
import com.github.andre10dias.spring_rest_api.integrationtests.testcontainers.dto.PersonDTO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static PersonDTO personDto;

    @BeforeAll // Executed before all tests
    static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        /*
        * Tratando o tipo LocalDate:
        *
        * O Spring Boot usa Jackson para serialização/desserialização.
        * Mas, por padrão, ele não serializa LocalDate automaticamente
        * como texto ISO-8601 sem uma configuração extra.
        * */
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        personDto = new PersonDTO();
    }

    @Test
    @Order(5)
    void findAll() throws JsonProcessingException {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.ORIGIN_HEADER, TestConfig.ANGULAR_ORIGIN_VALUE)
                .setBasePath("/api/people/v1")
                .setPort(TestConfig.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body().asString();

        List<PersonDTO> personDtoList = objectMapper.readValue(content, new TypeReference<List<PersonDTO>>() {});

        assertNotNull(personDtoList);
        assertFalse(personDtoList.isEmpty());

        boolean found = personDtoList.stream().anyMatch(person ->
                person.getFirstName().equals("First Name Test") &&
                        person.getLastName().equals("Last Name Test") &&
                        person.getAddress().equals("Address Test") &&
                        person.getGender().equals("Male")
        );

        assertTrue(found, "Expected person not found in the list.");
    }

    @Test
    @Order(3)
    void findById() throws JsonProcessingException {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.ORIGIN_HEADER, TestConfig.LOCAL_ORIGIN_VALUE)
                .setBasePath("/api/people/v1")
                .setPort(TestConfig.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", personDto.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body().asString();

        personDto = objectMapper.readValue(content, PersonDTO.class);

        assertEquals("First Name Test", personDto.getFirstName());
        assertEquals("Last Name Test", personDto.getLastName());
        assertEquals("Address Test", personDto.getAddress());
        assertEquals("Male", personDto.getGender());
    }

    @Test
    @Order(4)
    void findByIdWithWrongOrigin() {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.ORIGIN_HEADER, TestConfig.WRONG_ORIGIN_VALUE)
                .setBasePath("/api/people/v1")
                .setPort(TestConfig.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", personDto.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(403)
                .extract()
                .body().asString();

        assertEquals("Invalid CORS request", content);
    }

    @Test
    @Order(1)
    void create() throws JsonProcessingException {
        mockPerson();
        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.ORIGIN_HEADER, TestConfig.ANGULAR_ORIGIN_VALUE)
                .setBasePath("/api/people/v1")
                .setPort(TestConfig.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(personDto)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body().asString();

        personDto = objectMapper.readValue(content, PersonDTO.class);

        assertNotNull(personDto.getId());
        assertNotNull(personDto.getFirstName());
        assertNotNull(personDto.getLastName());
        assertNotNull(personDto.getAddress());
        assertNotNull(personDto.getGender());

        assertTrue(personDto.getId() > 0);

        assertEquals("First Name Test", personDto.getFirstName());
        assertEquals("Last Name Test", personDto.getLastName());
        assertEquals("Address Test", personDto.getAddress());
        assertEquals("Male", personDto.getGender());
    }

    @Test
    @Order(2)
    void createWithWrongOrigin() {
        mockPerson();
        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.ORIGIN_HEADER, TestConfig.WRONG_ORIGIN_VALUE)
                .setBasePath("/api/people/v1")
                .setPort(TestConfig.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(personDto)
                .when()
                .post()
                .then()
                .statusCode(403)
                .extract()
                .body().asString();

        assertEquals("Invalid CORS request", content);
    }

    @Test
    @Order(6)
    void update() throws JsonProcessingException {
        personDto.setFirstName("Updated Name");

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(personDto)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract()
                .body().asString();

        PersonDTO updatedPerson = objectMapper.readValue(content, PersonDTO.class);

        assertNotNull(updatedPerson.getId());
        assertEquals(personDto.getFirstName(), updatedPerson.getFirstName());
    }

    @Test
    @Order(7)
    void delete() {
        given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", personDto.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(8)
    void CreateV2() throws JsonProcessingException {
        PersonDTOv2 personDtoV2 = new PersonDTOv2();
        personDtoV2.setFirstName("First Name Test");
        personDtoV2.setLastName("Last Name Test");
        personDtoV2.setAddress("Address Test");
        personDtoV2.setGender("Male");
        personDtoV2.setBirthday(LocalDate.of(2001, 1, 1));

        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.ORIGIN_HEADER, TestConfig.ANGULAR_ORIGIN_VALUE)
                .setBasePath("/api/people/v1")
                .setPort(TestConfig.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(personDtoV2)
                .when()
                .post("/v2")
                .then()
                .statusCode(200)
                .extract()
                .body().asString();

        personDtoV2 = objectMapper.readValue(content, PersonDTOv2.class);

        assertNotNull(personDtoV2.getId());
        assertNotNull(personDtoV2.getFirstName());
        assertNotNull(personDtoV2.getLastName());
        assertNotNull(personDtoV2.getAddress());
        assertNotNull(personDtoV2.getGender());
        assertNotNull(personDtoV2.getBirthday());

        assertTrue(personDtoV2.getId() > 0);

        assertEquals("First Name Test", personDtoV2.getFirstName());
        assertEquals("Last Name Test", personDtoV2.getLastName());
        assertEquals("Address Test", personDtoV2.getAddress());
        assertEquals("Male", personDtoV2.getGender());
        assertEquals(LocalDate.of(2001, 1, 1), personDtoV2.getBirthday());
    }

    private void mockPerson() {
        personDto.setFirstName("First Name Test");
        personDto.setLastName("Last Name Test");
        personDto.setAddress("Address Test");
        personDto.setGender("Male");
    }

}