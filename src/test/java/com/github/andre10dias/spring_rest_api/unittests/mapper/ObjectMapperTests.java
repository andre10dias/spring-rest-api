package com.github.andre10dias.spring_rest_api.unittests.mapper;

import com.github.andre10dias.spring_rest_api.data.dto.v1.PersonDTO;
import com.github.andre10dias.spring_rest_api.data.dto.v2.PersonDTOv2;
import com.github.andre10dias.spring_rest_api.unittests.mapper.mocks.MockPerson;
import com.github.andre10dias.spring_rest_api.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.andre10dias.spring_rest_api.mapper.ObjectMapper.parseListObject;
import static com.github.andre10dias.spring_rest_api.mapper.ObjectMapper.parseObject;
import static com.github.andre10dias.spring_rest_api.mapper.custom.PersonMapper.toPersonDTOv2List;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ObjectMapperTests {
    MockPerson inputObject;

    @BeforeEach
    void setUp() {
        inputObject = new MockPerson();
    }

    @Test
    @DisplayName("Deve converter corretamente uma entidade Person em DTO")
    void parseEntityToDTOTest() {
        var dto = parseObject(inputObject.mockEntity(), PersonDTO.class);

        assertAll("PersonDTO",
                () -> assertEquals(0L, dto.getId()),
                () -> assertEquals("First Name Test0", dto.getFirstName()),
                () -> assertEquals("Last Name Test0", dto.getLastName()),
                () -> assertEquals("Address Test0", dto.getAddress()),
                () -> assertEquals("Male", dto.getGender()),
                () -> assertEquals(true, dto.isEnabled())
        );
    }

    @Test
    void parseEntityListToDTOListTest() {
        List<PersonDTOv2> outputList = toPersonDTOv2List(inputObject.mockEntityList());
        PersonDTOv2 outputZero = outputList.getFirst();

        assertEquals(0L, outputZero.getId());
        assertEquals("First Name Test0", outputZero.getFirstName());
        assertEquals("Last Name Test0", outputZero.getLastName());
        assertEquals("Address Test0", outputZero.getAddress());
        assertEquals("Male", outputZero.getGender());

        PersonDTOv2 outputSeven = outputList.get(7);

        assertEquals(7L, outputSeven.getId());
        assertEquals("First Name Test7", outputSeven.getFirstName());
        assertEquals("Last Name Test7", outputSeven.getLastName());
        assertEquals("Address Test7", outputSeven.getAddress());
        assertEquals("Female", outputSeven.getGender());
        PersonDTOv2 outputTwelve = outputList.get(12);

        assertEquals(12L, outputTwelve.getId());
        assertEquals("First Name Test12", outputTwelve.getFirstName());
        assertEquals("Last Name Test12", outputTwelve.getLastName());
        assertEquals("Address Test12", outputTwelve.getAddress());
        assertEquals("Male", outputTwelve.getGender());
    }

    @Test
    void parseDTOToEntityTest() {
        Person output = parseObject(inputObject.mockDTO(), Person.class);
        assertEquals(0L, output.getId());
        assertEquals("First Name Test0", output.getFirstName());
        assertEquals("Last Name Test0", output.getLastName());
        assertEquals("Address Test0", output.getAddress());
        assertEquals("Male", output.getGender());
        assertEquals(true, output.isEnabled());
    }

    @Test
    void parserDTOListToEntityListTest() {
        List<Person> outputList = parseListObject(inputObject.mockDTOList(), Person.class);
        Person outputZero = outputList.getFirst();

        assertEquals(0L, outputZero.getId());
        assertEquals("First Name Test0", outputZero.getFirstName());
        assertEquals("Last Name Test0", outputZero.getLastName());
        assertEquals("Address Test0", outputZero.getAddress());
        assertEquals("Male", outputZero.getGender());
        assertEquals(true, outputZero.isEnabled());

        Person outputSeven = outputList.get(7);

        assertEquals(7L, outputSeven.getId());
        assertEquals("First Name Test7", outputSeven.getFirstName());
        assertEquals("Last Name Test7", outputSeven.getLastName());
        assertEquals("Address Test7", outputSeven.getAddress());
        assertEquals("Female", outputSeven.getGender());
        assertEquals(true, outputZero.isEnabled());

        Person outputTwelve = outputList.get(12);

        assertEquals(12L, outputTwelve.getId());
        assertEquals("First Name Test12", outputTwelve.getFirstName());
        assertEquals("Last Name Test12", outputTwelve.getLastName());
        assertEquals("Address Test12", outputTwelve.getAddress());
        assertEquals("Male", outputTwelve.getGender());
        assertEquals(true, outputZero.isEnabled());
    }
}