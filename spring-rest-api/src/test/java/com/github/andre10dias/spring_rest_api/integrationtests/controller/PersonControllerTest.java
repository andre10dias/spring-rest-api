package com.github.andre10dias.spring_rest_api.integrationtests.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.andre10dias.spring_rest_api.config.TestConfig;
import com.github.andre10dias.spring_rest_api.data.dto.v2.PersonDTOv2;
import com.github.andre10dias.spring_rest_api.integrationtests.testcontainers.AbstractIntegrationTest;
import com.github.andre10dias.spring_rest_api.integrationtests.testcontainers.dto.PersonDTO;
import com.github.andre10dias.spring_rest_api.integrationtests.testcontainers.dto.security.AccountCredentialsDTO;
import com.github.andre10dias.spring_rest_api.integrationtests.testcontainers.dto.security.TokenDTO;
import com.github.andre10dias.spring_rest_api.integrationtests.testcontainers.dto.wrapper.WrapperPersonDTO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
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
    private static TokenDTO tokenDto;

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
        tokenDto = new TokenDTO();
    }

    @Test
    @Order(0)
//    @Disabled
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
    @Order(1)
    void create() throws JsonProcessingException {
        mockPerson();
        specification = createSpec(TestConfig.ANGULAR_ORIGIN_VALUE);

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
        specification = createSpec(TestConfig.WRONG_ORIGIN_VALUE);

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
    @Order(3)
    void findById() throws JsonProcessingException {
        specification = createSpec(TestConfig.LOCAL_ORIGIN_VALUE);

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
        specification = createSpec(TestConfig.WRONG_ORIGIN_VALUE);

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
    @Order(5)
//    @Disabled("REASON: Still under development.")
    void findAll() throws JsonProcessingException {
        specification = createSpec(TestConfig.ANGULAR_ORIGIN_VALUE);

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParams(
                        "page", 3,
                        "limit", 5,
                        "direction", "desc",
                        "orderBy", "firstName"
                )
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body().asString();

        WrapperPersonDTO wrapper = objectMapper.readValue(content, WrapperPersonDTO.class);
        List<PersonDTO> personList = wrapper.getEmbedded().getPeopleDto();
        PersonDTO person = personList.getFirst();

        assertNotNull(person);
        assertNotNull(person.getId());
        assertNotNull(person.getFirstName());
        assertNotNull(person.getLastName());
        assertNotNull(person.getGender());
        assertNotNull(person.getProfileUrl());
        assertNotNull(person.getPhotoUrl());

        JsonNode root = objectMapper.readTree(content);

        // HAL _embedded.personDTOList
        JsonNode embeddedNode = root.path("_embedded");
        JsonNode personListNode = embeddedNode.path("people"); // nome padrão da coleção no HAL

        personList = objectMapper.readValue(
                personListNode.toString(),
                new TypeReference<List<PersonDTO>>() {}
        );

        // Valida ordenação desc por firstName
        for (int i = 0; i < personList.size() - 1; i++) {
            String current = personList.get(i).getFirstName();
            String next = personList.get(i + 1).getFirstName();
            assertTrue(current.compareToIgnoreCase(next) >= 0, "Lista não está em ordem decrescente");
        }

        // Valida metadados da página
        JsonNode pageNode = root.path("page");
        assertEquals(2, pageNode.path("number").asInt()); // page-1
        assertEquals(5, pageNode.path("size").asInt());
    }

    @Test
    @Order(6)
    void findPeopleByFirstName() throws JsonProcessingException {
        specification = createSpec(TestConfig.ANGULAR_ORIGIN_VALUE);

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("firstName", "ala")
                .queryParams(
                        "direction", "desc",
                        "orderBy", "lastName"
                )
                .when()
                .get("firstName/{firstName}")
                .then()
                .statusCode(200)
                .extract()
                .body().asString();

        WrapperPersonDTO wrapper = objectMapper.readValue(content, WrapperPersonDTO.class);
        List<PersonDTO> personList = wrapper.getEmbedded().getPeopleDto();
        PersonDTO person = personList.getFirst();

        assertNotNull(person);
        assertNotNull(person.getId());
        assertNotNull(person.getFirstName());
        assertNotNull(person.getLastName());
        assertNotNull(person.getGender());

        JsonNode root = objectMapper.readTree(content);

        // HAL _embedded.personDTOList
        JsonNode embeddedNode = root.path("_embedded");
        JsonNode personListNode = embeddedNode.path("people"); // nome padrão da coleção no HAL

        personList = objectMapper.readValue(
                personListNode.toString(),
                new TypeReference<List<PersonDTO>>() {}
        );

        // Valida ordenação desc por firstName
        for (int i = 0; i < personList.size() - 1; i++) {
            String current = personList.get(i).getLastName();
            String next = personList.get(i + 1).getLastName();
            assertTrue(current.compareToIgnoreCase(next) >= 0, "Lista não está em ordem decrescente");
            assertTrue(personList.get(i).getFirstName().toLowerCase().contains("ala"), "Lista nao contem 'ala'");
        }
    }

    @Test
    @Order(7)
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
    @Order(8)
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
    @Order(9)
    void testAccessWithInvalidToken() {
        given()
                .header(
                        TestConfig.AUTHORIZATION_HEADER,
                        TestConfig.INVALID_TOKEN
                )
                .contentType(ContentType.JSON)
                .port(TestConfig.SERVER_PORT)
                .basePath("/api/person/v1")
                .when()
                .get()
                .then()
                .statusCode(403); //Forbidden
    }


    @Test
    @Order(10)
    void testAccessProtectedEndpointWithoutToken() {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .port(TestConfig.SERVER_PORT)
                .basePath("/api/person/v1")
                .when()
                .get()
                .then()
                .statusCode(403);
    }


    @Test
    @Order(11)
    void CreateV2() throws JsonProcessingException {
        PersonDTOv2 personDtoV2 = new PersonDTOv2();
        personDtoV2.setFirstName("First Name Test");
        personDtoV2.setLastName("Last Name Test");
        personDtoV2.setAddress("Address Test");
        personDtoV2.setGender("Male");
        personDtoV2.setBirthday(LocalDate.of(2001, 1, 1));

        specification = createSpec(TestConfig.ANGULAR_ORIGIN_VALUE);

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
        personDto.setEnabled(true);
    }

    private static RequestSpecification createSpec(String origin) {
        if (tokenDto == null || tokenDto.getAccessToken() == null) {
            throw new IllegalStateException("Token de acesso não foi inicializado. Verifique AuthHelper.signInDefaultUser().");
        }

        return new RequestSpecBuilder()
                .addHeader(TestConfig.ORIGIN_HEADER, origin)
                .addHeader(
                        TestConfig.AUTHORIZATION_HEADER,
                        TestConfig.BEARER_TOKEN_PREFIX + tokenDto.getAccessToken()
                )
                .setBasePath("/api/people/v1")
                .setPort(TestConfig.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

}