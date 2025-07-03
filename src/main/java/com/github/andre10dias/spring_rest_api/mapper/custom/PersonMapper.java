package com.github.andre10dias.spring_rest_api.mapper.custom;

import com.github.andre10dias.spring_rest_api.data.dto.v2.PersonDTOv2;
import com.github.andre10dias.spring_rest_api.model.Person;

import java.util.List;

public class PersonMapper {
    
    private PersonMapper() {
        // Private constructor to prevent instantiation
    }

    public static PersonDTOv2 toPersonDTOv2(Person person) {
        return new PersonDTOv2(
                person.getId(),
                person.getFirstName(),
                person.getLastName(),
                person.getAddress(),
                person.getGender(),
                person.getBirthday()
        );
    }

    public static Person toPerson(PersonDTOv2 personDTOv2) {
        return new Person(
                personDTOv2.getId(),
                personDTOv2.getFirstName(),
                personDTOv2.getLastName(),
                personDTOv2.getAddress(),
                personDTOv2.getGender(),
                personDTOv2.getBirthday()
        );
    }

    public static List<PersonDTOv2> toPersonDTOv2List(List<Person> personList) {
        return personList.stream().map(PersonMapper::toPersonDTOv2).toList();
    }

}
