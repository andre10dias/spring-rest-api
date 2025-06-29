package com.github.andre10dias.spring_rest_api.mapper.mocks;

import com.github.andre10dias.spring_rest_api.data.dto.PersonDTO;
import com.github.andre10dias.spring_rest_api.model.Person;

import java.util.ArrayList;
import java.util.List;

public class MockPerson {

    public Person mockEntity() {
        return mockEntity(0);
    }
    
    public PersonDTO mockDTO() {
        return mockDTO(0);
    }
    
    public List<Person> mockEntityList() {
        List<Person> persons = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            persons.add(mockEntity(i));
        }
        return persons;
    }

    public List<PersonDTO> mockDTOList() {
        List<PersonDTO> persons = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            persons.add(mockDTO(i));
        }
        return persons;
    }
    
    public Person mockEntity(Integer number) {
        return new Person(
                number.longValue(),
                "First Name Test" + number,
                "Last Name Test" + number,
                "Address Test" + number,
                ((number % 2)==0) ? "Male" : "Female"
        );
    }

    public PersonDTO mockDTO(Integer number) {
        return new PersonDTO(
                number.longValue(),
                "First Name Test" + number,
                "Last Name Test" + number,
                "Address Test" + number,
                ((number % 2)==0) ? "Male" : "Female"
        );
    }

}