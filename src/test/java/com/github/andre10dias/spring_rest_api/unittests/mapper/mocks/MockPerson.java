package com.github.andre10dias.spring_rest_api.unittests.mapper.mocks;

import com.github.andre10dias.spring_rest_api.data.dto.v1.PersonDTO;
import com.github.andre10dias.spring_rest_api.model.Person;

import java.time.LocalDate;
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
            var person = mockEntity(i);
            persons.add(person);
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
        int monthNumber = number <= 11 ? number : 0;
        int dayNumber = (monthNumber == 1 && number <= 27) || (monthNumber != 1 && number <= 29) ? number : 0;
        return new Person(
                number.longValue(),
                "First Name Test" + number,
                "Last Name Test" + number,
                "Address Test" + number,
                ((number % 2)==0) ? "Male" : "Female",
                LocalDate.of(1950 + number, 1 + monthNumber, 1 + dayNumber)
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